package lab.uro.kitori.samplehilt.presentation

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import lab.uro.kitori.samplehilt.databinding.ActivityUserBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeData()

        viewModel.getUser()
    }

    private fun observeData() {
        viewModel.user.observe(this) {
            it ?: return@observe

            binding.name.text = it.name
            binding.url.text = it.url
        }

        viewModel.error.observe(this) {
            it ?: return@observe

            AlertDialog.Builder(this)
                .setTitle("error")
                .setMessage(it)
                .show()
        }
    }
}
