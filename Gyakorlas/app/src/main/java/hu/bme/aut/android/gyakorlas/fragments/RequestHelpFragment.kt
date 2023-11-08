package hu.bme.aut.android.gyakorlas.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentRequestHelpBinding


class RequestHelpFragment : Fragment() {
    private lateinit var binding : FragmentRequestHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestHelpBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pulseAnimation = AnimationUtils.loadAnimation(this.context, R.anim.pulse_animation)
        var isPulsing = false

        binding.ibRequestHelp.setOnClickListener() {
            //findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
            if (!isPulsing) {
                binding.ibRequestHelp.startAnimation(pulseAnimation)
                isPulsing = true
            }
            else {
                binding.ibRequestHelp.clearAnimation()
                isPulsing = false
            }
        }
        binding.imgbtnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_requestHelpFragment_to_menuFragment)
        }
    }

}
