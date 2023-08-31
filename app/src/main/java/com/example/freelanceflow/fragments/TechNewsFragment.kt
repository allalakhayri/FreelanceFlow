    package com.example.freelanceflow.fragments

    import android.os.Bundle

    import android.view.View
    import android.widget.LinearLayout
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.Observer
    import androidx.lifecycle.ViewModelProvider
    import androidx.recyclerview.widget.DividerItemDecoration
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.example.freelanceflow.R
    import com.example.freelanceflow.activities.MainActivity
    import com.example.freelanceflow.adapters.BloggerAdapter

    import com.example.freelanceflow.databinding.FragmentTechNewsBinding
    import com.example.freelanceflow.repository.TechNewsRepository
    import com.example.freelanceflow.util.ViewModelSetupUtil
    import com.example.freelanceflow.viewmodels.TechNewsViewModel
    import com.example.freelanceflow.viewmodels.TechNewsViewModelFactory

    class TechNewsFragment : Fragment(R.layout.fragment_tech_news) {
        private val bloggerAdapter = BloggerAdapter()
        private lateinit var blogBinding: FragmentTechNewsBinding
       // private lateinit var techNewsViewModel: TechNewsViewModel
        private val techNewsViewModel: TechNewsViewModel by viewModels {
            TechNewsViewModelFactory(requireActivity().application, TechNewsRepository())
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
          //  techNewsViewModel = ViewModelSetupUtil.setupTechNewsViewModel(requireActivity())

            blogBinding = FragmentTechNewsBinding.bind(view)

            setUpRecyclerView()
        }
        private fun setUpRecyclerView() {
            blogBinding.rvBlogs.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = bloggerAdapter
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            }
            observeData()
        }

        private fun observeData() {
            techNewsViewModel.getTechnologyNews().observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    bloggerAdapter.submitList(it.articles)
                }
            })
        }
    }
