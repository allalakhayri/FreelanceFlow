    package com.example.freelanceflow.fragments


    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.navigation.findNavController
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.bumptech.glide.Glide
    import com.example.freelanceflow.R
    import com.example.freelanceflow.adapters.PostsAdapter

    import com.example.freelanceflow.databinding.FragmentPostsBinding

    import com.example.freelanceflow.users.Post
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener


    class PostsFragment : Fragment(R.layout.fragment_posts) {
        private lateinit var connectBinding: FragmentPostsBinding
        private lateinit var postsAdapter: PostsAdapter
        private lateinit var firebaseDatabase: FirebaseDatabase
        private lateinit var postList: ArrayList<Post>
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            connectBinding = FragmentPostsBinding.bind(view)
            firebaseDatabase = FirebaseDatabase.getInstance()

            connectBinding.fabCreatePost.setOnClickListener {
                val direction = PostsFragmentDirections.actionPostsFragmentToAddPostFragment()
                findNavController().navigate(direction)
            }
            viewPosts()
            setUpRecyclerView()
        }

        private fun setUpRecyclerView() {
            postsAdapter = PostsAdapter(Glide.with(this))
            connectBinding.rvPosts.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }



        private fun viewPosts(){
            val reference = firebaseDatabase.getReference("Post")
            postList = ArrayList()
            reference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postsnap in snapshot.children){
                        val post = postsnap.getValue(Post::class.java)

                        if (post != null) {
                            postList.add(post)
                        }

                    }
                    postsAdapter.postDiffer.submitList(postList)

                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "$error Error Occurred! ", Toast.LENGTH_LONG).show()
                }
            })
        }


    }

