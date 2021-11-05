package com.rawezh.noteapp

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rawezh.noteapp.database.NoteDatabase
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.rawezh.noteapp.entities.Notes
import com.rawezh.noteapp.util.NoteBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_create_note.imgMore
import kotlinx.android.synthetic.main.fragment_notes_buttom_sheet.*
import kotlinx.android.synthetic.main.item_rv_notes.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception

class CreateNoteFragment : BaseFragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private var READ_STORAGE_PER = 123
    private var REQUEST_STORAGE_IMAGE = 456
    private var selectedImagePath = ""
    private var webUrlLink = ""

    var selectedColor = "#171C26"
    var currentDate:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")


        currentDate = date.format(Date())
        colorView.setBackgroundColor(Color.parseColor(selectedColor))

        noteDate.text = currentDate

        imgDone.setOnClickListener {
            saveNote()
        }

        imgBack.setOnClickListener {
           requireActivity().supportFragmentManager.popBackStack()
        }

        imgMore.setOnClickListener {
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance()
            noteBottomSheetFragment.show(requireActivity().supportFragmentManager, "Note Bottom Sheet Fragment")
        }

        btn_ok.setOnClickListener{
            if(Note_Text_Web.text.toString().trim().isEmpty()){
                checkWebUri()
            }else {
                Toast.makeText(requireContext(),"Url is required!",Toast.LENGTH_LONG ).show()
            }
        }

        btn_cancle.setOnClickListener{
            layoutWebLink.visibility = View.GONE
        }

        Link_Visible_Note.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(Note_Text_Web.text.toString()))
            startActivity(intent)
        }
    }


    private fun saveNote() {
        if(noteTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Title Required", Toast.LENGTH_SHORT).show()
        }
        else if(noteSubTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Sub Title Required", Toast.LENGTH_SHORT).show()
        }
        else if(noteText.text.isNullOrEmpty()) {
            Toast.makeText(context, "Text Required", Toast.LENGTH_SHORT).show()
        }
        else {
        launch {
            var notes = Notes()
            notes.title = noteTitle.text.toString()
            notes.subTitle = noteSubTitle.text.toString()
            notes.noteText = noteText.text.toString()
            notes.dateTime = currentDate
            notes.color = selectedColor
            notes.imgPath = selectedImagePath
            notes.webLink = webUrlLink
            context?.let {
                NoteDatabase.getDatabase(it).noteDao().insertNotes(notes)
                noteTitle.setText("")
                noteSubTitle.setText("")
                noteText.setText("")
                imgNote.visibility = View.GONE
                Link_Visible_Note.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        }

    }

    private fun checkWebUri(){
        if(Patterns.WEB_URL.matcher(Note_Text_Web.text.toString()).matches()){
            layoutWebLink.visibility = View.GONE
            Note_Text_Web.isEnabled = false
            webUrlLink = Note_Text_Web.text.toString()
            Link_Visible_Note.visibility = View.VISIBLE
            Link_Visible_Note.text = Note_Text_Web.text.toString()
        } else {
            Toast.makeText(requireContext(), "Url is not Valid",Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun replaceFragment(fragment: Fragment, istransition:Boolean){
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }


    private val BroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionColor = p1!!.getStringExtra("action")

            when(actionColor!!) {
                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Image" -> {
                    readStorageTask()
                    layoutWebLink.visibility = View.GONE
                }

                "WebLink" -> {
                    layoutWebLink.visibility = View.VISIBLE
                }
                else -> {
                    imgNote.visibility = View.GONE
                    layoutWebLink.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }

    private fun hasWriteStoragePermission():Boolean {
        return EasyPermissions.hasPermissions(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun hasReadStoragePermission():Boolean {
        return EasyPermissions.hasPermissions(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask(){
        if(hasReadStoragePermission()){
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_LONG).show()
            pickImageFromStorage()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PER,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromStorage(){
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if(intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent,REQUEST_STORAGE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri:Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if(cursor == null){
            filePath = contentUri.path
        }else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_STORAGE_IMAGE && requestCode == RESULT_OK){
            if(data != null){
                var selectedImageUrl = data.data
                if(selectedImageUrl != null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        imageNote.setImageBitmap(bitmap)
                        imageNote.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)!!

                    }catch (e:Exception){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,requestCode)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }


}