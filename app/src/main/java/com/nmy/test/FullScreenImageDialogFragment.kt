package com.nmy.test

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nmy.test.databinding.ActivityMainBinding
import com.nmy.test.databinding.DialogFullScreenImageBinding

class FullScreenImageDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFullScreenImageBinding

    companion object {
        private const val IMAGE_BITMAP = "image_bitmap"

        fun newInstance(image: Bitmap): FullScreenImageDialogFragment {
            val fragment = FullScreenImageDialogFragment()
            val args = Bundle()
            args.putParcelable(IMAGE_BITMAP, image)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFullScreenImageBinding.inflate(layoutInflater)

        // 이미지 설정
        val image = arguments?.getParcelable<Bitmap>(IMAGE_BITMAP)
        binding.fullScreenImageView.setImageBitmap(image)


        // 화면을 터치하면 다이얼로그 닫히게 처리
        binding.root.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.black)  // 배경을 어두운 색으로
        return dialog
    }
}