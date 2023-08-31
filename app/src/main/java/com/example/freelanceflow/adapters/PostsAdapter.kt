package com.example.freelanceflow.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.freelanceflow.databinding.ItemPostBinding
import com.example.freelanceflow.R
import com.example.freelanceflow.fragments.PostsFragmentDirections


import com.example.freelanceflow.users.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostsAdapter(private val glide: RequestManager) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    var isLiked = false

    private val postCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.uId == newItem.uId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    val postDiffer = AsyncListDiffer(this, postCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

        val itemPosition = postDiffer.currentList[position]
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid
        val likesReference = FirebaseDatabase.getInstance().getReference("likes")
        val postKey = itemPosition.postKey
        holder.apply {
            userName.text = itemPosition.email
            description.text = itemPosition.description
            title.text=itemPosition.title
            readTime= itemPosition.readTime!!
            Glide.with(itemView)
                .load(itemPosition.image)
                .into(image)
            itemView.setOnClickListener {
                val direction = PostsFragmentDirections.actionPostsFragmentToPostDetailsFragment(itemPosition, postKey!!)
                it.findNavController().navigate(direction)
            }

            if (postKey != null) {
                postLikedStatus(currentUserId!!, postKey)
            }


            likedButton.setOnClickListener {
                isLiked = true

                likesReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (isLiked) {
                            if (snapshot.child(postKey!!).hasChild(currentUserId!!)) {
                                likesReference.child(postKey).child(currentUserId).removeValue()
                                isLiked = false
                            } else {
                                likesReference.child(postKey).child(currentUserId).setValue(true)
                                isLiked = false
                            }


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


            }
        }
    }

    override fun getItemCount(): Int {
        return postDiffer.currentList.size
    }

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postBinding = ItemPostBinding.bind(itemView)
        val userName = postBinding.authorTextView
        val title=postBinding.postTitletv
        val description = postBinding.postDescription
        val image = postBinding.postImage
        val likedButton = postBinding.imgLike
        val commentButton = postBinding.imgComment
        val likesNumber = postBinding.tvLikes
        val readTimeString=postBinding.readTimetv.text.toString()
        var readTime=readTimeString.toIntOrNull() ?: 0
        fun postLikedStatus(currentUserId: String, postKey: String) {

            val likesReference = FirebaseDatabase.getInstance().getReference("likes")

            likesReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(postKey).hasChild(currentUserId)) {
                        val likesCount = snapshot.child(postKey).childrenCount.toInt()
                        likedButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                        likesNumber.text = "${likesCount} Likes"
                    } else {
                        val likesCount = snapshot.child(postKey).childrenCount.toInt()
                        likedButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        likesNumber.text = "${likesCount} Likes"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


    }
}
