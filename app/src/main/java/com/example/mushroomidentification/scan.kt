package com.example.mushroomidentification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mushroomidentification.databinding.FragmentScanBinding
import com.example.mushroomidentification.ml.FinalModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [scan.newInstance] factory method to
 * create an instance of this fragment.
 */
class scan : Fragment(R.layout.fragment_scan) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentScanBinding
    private lateinit var imageView: ImageView
    private lateinit var buttonload: Button
    private lateinit var tvOutput: TextView
    private lateinit var textview: TextView
    private lateinit var text_desc: TextView
    private val GALLERY_REQUEST_CODE = 123

    private lateinit var button: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View=inflater.inflate(R.layout.fragment_scan, container, false)
        button = view.findViewById(R.id.btn_capture_image)
        button.setOnClickListener{
            if (context?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.CAMERA
                    )
                } == PackageManager.PERMISSION_GRANTED) {
                takePicturePreview.launch(null)
            } else {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
        buttonload = view.findViewById(R.id.btn_load_image)
        buttonload.setOnClickListener {
            if (context?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
                == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            } else {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        tvOutput = view.findViewById(R.id.tv_output)
        textview = view.findViewById(R.id.textview)
        text_desc = view.findViewById(R.id.description_text)
        // to redirect user to google
        tvOutput.setOnClickListener {
            val index = tvOutput.text.indexOf("-")
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=${tvOutput.text.substring(0,index -1)}")
            )
            startActivity(intent)
        }
        // to download image when longPress on ImageView
        imageView = view.findViewById(R.id.imageView)
        imageView.setOnLongClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            return@setOnLongClickListener true
        }
        return view
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment scan.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            scan().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = FragmentScanBinding.inflate(layoutInflater)
        val imageView = binding.imageView
        button = binding.btnCaptureImage
        tvOutput = binding.tvOutput
        val buttonLoad = binding.btnLoadImage
    }

    //request camera permission
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePicturePreview.launch(null)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Permission denied! Try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // launch camera and take pictures
    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
                outputGenerator(bitmap)
            }
        }

    // to get image from gallery
    private val onresult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResultReceived(GALLERY_REQUEST_CODE, result)
        }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?) {
        var resolver = requireActivity().contentResolver

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (result?.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap =
                            BitmapFactory.decodeStream(resolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap)
                        outputGenerator(bitmap)
                    }
                } else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun outputGenerator(bitmap: Bitmap) {
        val model = context?.let { FinalModel.newInstance(it) }

// Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

        var resize: Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(224 * 224)
        resize.getPixels(intValues, 0, resize.width, 0, 0, resize.width, resize.height)
        var pixel = 0
        for (i in 0..223) {
            for (j in 0..223) {
                var v = intValues[pixel++] // RGB
                byteBuffer.putFloat(((v shr 16) and 0xFF) * (1.0f / 255.0f))
                byteBuffer.putFloat(((v shr 8) and 0xFF) * (1.0f / 255.0f))
                byteBuffer.putFloat((v and 0xFF) * (1.0f / 255.0f))
            }
        }
        Log.d("shape", byteBuffer.toString())
        Log.d("shape", inputFeature0.buffer.toString())
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model!!.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray
        var maxPos = 0
        var maxConfidence = 0.0f
        print(confidences.size)
        for (i in 0..confidences.size - 1) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        val common_name = arrayOf<String>(
            "Autumn Skullcap",
            "Button Mushroom",
            "Common Split Gill",
            "Crown-tipped coral",
            "Death Cap",
            "False Parasol",
            "Jack O' Lantern",
            "Lion's Mane",
            "Magic Mushroom",
            "Maitake Mushroom",
            "Mower's Mushroom",
            "Oyster Mushroom",
            "The Fly Agaric",
            "Turkey tail",
            "Yellow Field Cap",
            "Yellow Patches")

        val scientific_name = arrayOf<String>(
            "Galerina marginata",
            "Agaricus bisporus",
            "Schizophyllum commune",
            "Artomyces pyxidatus",
            "Amanita phalloides",
            "Chlorophyllum molybdites",
            "Omphalotus illudens",
            "Hericium erinaceus",
            "Psilocybe cubensis",
            "Grifola frondosa",
            "Panaeolina foenisecii",
            "Pleurotus ostreatus",
            "Amanita muscaria",
            "Trametes versicolor",
            "Bolbitius titubans",
            "Amenita flavoconia")

        val values = arrayOf<String>(
            "Poisonous!",
            "Non Poisonous",
            "Non Poisonous",
            "Non Poisonous",
            "Poisonous!",
            "Poisonous!",
            "Poisonous!",
            "Non Poisonous",
            "Poisonous!",
            "Non Poisonous",
            "Non Poisonous",
            "Non Poisonous",
            "Poisonous!",
            "Non Poisonous",
            "Non Poisonous",
            "Poisonous!")

        val description = arrayOf<String>(
            "Also known as - funeral bell, deadly skullcap, autumn skullcap or deadly galerina. Galerina marginata,is a species of extremely poisonous mushroom-forming fungus in the family Hymenogastraceae of the order Agaricales. It contains the same deadly amatoxins found in the death cap\n",
            "Agaricus bisporus is an edible basidiomycete mushroom native to grasslands in Eurasia and North America. It has two color states while immature – white and brown – both of which have various names, with additional names for the mature state. It is the most familiar mushroom for most of us — it is the commercial mushroom sold in grocery stores and put on pizza.\n",
            "Schizophyllum commune is a species of fungus in the genus Schizophyllum. The mushroom resembles undulating waves of tightly packed corals or loose Chinese fan. \"Gillies\" or \"split gills\" vary from creamy yellow to pale white in colour. The cap is small, 1–4 centimetres wide with a dense yet spongey body texture.The species is edible and widely consumed in Mexico and some tropical countries.\n",
            "Artomyces pyxidatus, also known as the crown-tipped coral, is part of coral (clavarioid) fungi, which groups together all fungi that create upright branches as their fruiting bodies. It grows on wood (usually the wood of hardwoods); its colors, when fresh, are whitish to yellowish; and its branch tips are distinctively \"crowned,\" featuring a tiny cuplike depression surrounded by 3-6 points\n",
            "Amanita phalloides, also known as 'death cap', is one of the most poisonous mushrooms, being involved in the majority of human fatal cases of mushroom poisoning worldwide. This species contains three main groups of toxins: amatoxins, phallotoxins, and virotoxins.\n",
            "It is a widespread mushroom. Consumption causes severe gastrointestinal symptoms of vomiting and diarrhea, it is commonly conflated with the shaggy parasol (Chlorophyllum rhacodes) or shaggy mane (Coprinus comatus), and is the most commonly misidentified poisonous mushroom due to its large size and similarity to the edible parasol mushroom(Macrolepiota procera), as well as its habit of growing in areas near human habitation. The nature of the poisoning is predominantly gastrointestinal.\n",
            "This species is fairly easy to identify. It grows in clusters on wood, its colors are bright orange, its gills run down the stem, it has a white to pale yellow spore print, and its flesh, when sliced open, is orange. The Jack O'Lantern mushroom is sometimes confused with chanterelles --especially when it appears to be growing terrestrially rather than from wood.\n",
            "Also known as - lion's mane mushroom, mountain-priest mushroom, bearded tooth fungus, and bearded hedgehog. It is an edible mushroom belonging to the tooth fungus group. H. erinaceus can be mistaken for other species of Hericium, which grow across the same range. In the wild, these mushrooms are common during late summer and fall on hardwoods, particularly American beech and maple. Usually H. erinaceus is considered saprophytic, as it mostly feeds on dead trees. It can also be found on living trees, so may be a tree parasite.\n",
            "It is Hallucinogenic and also known as - magic mushrooms, golden halos, cubes, or gold caps. It is the best-known psilocybin mushroom due to its wide distribution and ease of cultivation. This mushroom being optimal for home cultivation specifically, as was suggested in the 1970s, is primarily what led to cubensis being the psilocybin mushroom species most common on the black market as a street drug. \n",
            "Grifola frondosa (G. frondosa), generally known as hen-of-the-woods or maitake in Japanese and hui-shu-hua in Chinese, is an edible mushroom with both nutritional and medicinal properties. Grifola frondosa is a polypore mushroom that grows at the base of trees, particularly old growth oaks or maples. It is typically found in late summer to early autumn. It is native to China, Europe, and North America.\n",
            "Also known as - Brown Mottlegill, Mower’s Mushroom and the Haymaker. Panaeolina foenisecii springs up on any lawn that is regularly mown but not intensively dosed with lots of artificial fertilisers or selective herbicides. It is probably the most common of the ‘common or garden’ lawn mushrooms, and unfortunately it is inedible and can cause sickness if eaten. (Toddlers are particularly vulnerable!)\n",
            "Also known as - Oyster Mushroom, oyster fungus, hiratake, or pearl oyster mushroom. leurotus ostreatus is easily recognized by the way it grows on wood in shelf-like clusters; its relatively large size; its whitish gills that run down a stubby, nearly-absent stem; and its whitish to lilac spore print. It appears between October and early April across North America, and features a brown cap. \n",
            "The Fly Agaric, Amanita muscaria, is a hallucinogen and must be considered poisonous. These attractive fungi often appear in groups and are a common sight in all kinds of woodlands.When they first emerge from the leaf litter of the forest floor, the young fruitbodies are covered entirely in pointed white warts, as seen here. As the caps expand the red pellicle shows through until eventually the cap comprises mainly red skin with white warts distributed more or less evenly across its surface. Heavy rain or even contact with animals is sometimes sufficient to remove some or all of the white flakes from the cap of a Fly Agaric, so you may see some 'bald' specimens.\n",
            "Coriolus versicolor, also known as Trametes versicolor and Polyporus versicolor, is a common polypore mushroom found throughout the world and also a well-known traditional medicinal mushroom growing on tree trunks.\n",
            "Bolbitius titubans, also known as Bolbitius vitellinus, is a widespread species of mushroom found in America and Europe. It grows chiefly on dung or heavily fertilized soil, and sometimes on grass. It is nonpoisonous. \n",
            "Also known as - yellow patches, yellow wart, orange amanita, yellow-dust amanita or the American yellow dust amanita. It is a species of mushroom in the family Amanitaceae. It has an orangish-yellow cap with yellowish-orange patches or warts, a yellowish-orange annulus, and a white to orange stem"


        )

        tvOutput.setText(common_name[maxPos]+ " - " + values[maxPos])
        val confidence:Double = Math.round(maxConfidence * 1000.0) / 1000.0
        textview.setText("Output:  "+ confidence*100+"%")

        text_desc.setText(description[maxPos])

        // Releases model resources if no longer used.
        model.close()

        //Inserting identified mushroom in the Table
        //creating the mush obj
        var mush = MushroomItem(common_name[maxPos]+ " - " + values[maxPos],scientific_name[maxPos],(confidence*100).toString())
        var db = context?.let { DatabaseHandler(it) }
        if (db != null) {
            db.insertData(mush)
        }
    }

    // to download the image to device
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                context?.let {
                    AlertDialog.Builder(it).setTitle("Download Image?")
                        .setMessage("Do you want to download this image to your device?")
                        .setPositiveButton("Yes") { _, _ ->
                            val drawable: BitmapDrawable = imageView.drawable as BitmapDrawable
                            val bitmap = drawable.bitmap
                            downloadImage(bitmap)
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            } else {
                fun Context.toast(message: CharSequence) =
                    Toast.makeText(this, "Please allow permission to download image", Toast.LENGTH_SHORT).show()
            }
        }

    // fun that takes a bitmap and store to users device
    private fun downloadImage(mBitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "Mushroom_Images" + System.currentTimeMillis() / 1000
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var resolver = requireActivity().contentResolver

        if (uri != null) {
            resolver.insert(uri, contentValues)?.also {
                resolver.openOutputStream(it).use { outputStream ->
                    if (!mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        throw IOException("Couldn't save the bitmap")
                    } else run {
                        fun Context.toast(message: CharSequence) =
                            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
                    }
                }
                return it
            }
        }
        return null
    }
}
