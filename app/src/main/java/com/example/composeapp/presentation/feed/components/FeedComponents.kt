package com.example.composeapp.presentation.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.composeapp.R
import com.example.composeapp.domain.model.CommentWithUser
import com.example.composeapp.domain.model.PostType
import com.example.composeapp.domain.model.PostWithUser
import com.example.composeapp.domain.model.User
import com.example.composeapp.presentation.Screen
import com.example.composeapp.presentation.UiEvent
import com.example.composeapp.presentation.feed.PostFeedViewModel
import com.example.composeapp.ui.theme.Blue
import com.example.composeapp.ui.theme.LightBlue
import com.example.composeapp.ui.theme.profilePickSize
import com.example.composeapp.ui.theme.standardPadding
import com.google.android.exoplayer2.ExoPlayer

@Composable
private fun LikeButton(text: String, modifier: Modifier = Modifier) {
    val checkedState = remember {
        mutableStateOf(true)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconToggleButton(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it},
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = if (checkedState.value) R.drawable.heart_on else R.drawable.heart_off),
                contentDescription = "Icon",
                tint = if (checkedState.value) Blue else Color.Black
            )
        }
        Text(
            text = text,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun BottomButton(
    text: String,
    resId: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = resId),
            contentDescription = "Icon",
            tint = Color.Black
        )
        Text(
            text = text,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun PostHeader(
    user: User,
    secondaryText:String,
    postType: PostType = PostType.QUESTION,
    showPostType: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = standardPadding, end = standardPadding, top = standardPadding/2)
    ) {
        AsyncImage(
            model = user.profilePicUrl,
            contentDescription = "profile_pick",
            modifier = Modifier
                .size(profilePickSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = standardPadding/2)
        ) {
            Row {
                Text(
                    text = user.fullName,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                )

                if(showPostType){
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        color = LightBlue
                    ) {
                        Text(
                            text = postType.toString(),
                            style = TextStyle(
                                color = Blue,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Text(
                text = secondaryText,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            )
        }

        Spacer(modifier = Modifier
            .height(10.dp)
            .weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.options),
            contentDescription = "options",
            modifier = Modifier
                .size(24.dp)
                .rotate(if (!showPostType) 90f else 0f)
        )
    }
}

@Composable
private fun PostBody(
    postWithUser: PostWithUser,
    exoPlayer: ExoPlayer?,
    isVideoPlaying: Boolean,
    onPlayVideoClick : ()->Unit
) {
    if(postWithUser.post.bodyText!=null){
        Text(
            text = postWithUser.post.bodyText,
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(horizontal = standardPadding, vertical = standardPadding/2)
        )
    }

    if(postWithUser.post.media!=null){
        PostMedia(
            media = postWithUser.post.media,
            isVideoPlaying = isVideoPlaying,
            exoPlayer = exoPlayer,
            onPlayButtonClick = onPlayVideoClick
        )
    }
}

@Composable
private fun PostFooter(
    postWithUser:PostWithUser,
    navController: NavController,
    viewModel: PostFeedViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    val likesCount = postWithUser.post.numLikes
    val commentsCount = postWithUser.post.numComments
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(standardPadding)
    ) {
        Surface(modifier = Modifier.weight(1.7f)) {
            LikeButton(text = "$likesCount likes")
        }
        Surface(modifier = Modifier
            .weight(2f)
            .clickable(interactionSource, null) {
                viewModel.onEvent(UiEvent.ViewPostComments(postWithUser))
                navController.navigate(Screen.CommentsScreenRoute)
            }) {
            BottomButton(text = "$commentsCount comments", resId = R.drawable.comment)
        }
        Surface(modifier = Modifier.weight(1f)) {
            BottomButton(text = "share", resId = R.drawable.share)
        }
    }
}

@Composable
private fun CommentsFooter(text:String) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = standardPadding)) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(3f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(1f)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_scroll),
                contentDescription = "icon",
                tint = Blue
            )
            Text(
                text = "Recent",
                style = TextStyle(
                    color = Blue,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@Composable
private fun CommentBody(comment:String) {
    Text(
        text = comment,
        style = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        ),
        modifier = Modifier.padding(horizontal = standardPadding, vertical = standardPadding/2)
    )
}

@Composable
fun Post(
    postWithUser: PostWithUser,
    exoPlayer: ExoPlayer?,
    isVideoPlaying: Boolean,
    onPlayVideoClick : ()->Unit,
    navController: NavController,
    viewModel: PostFeedViewModel
) {
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        PostHeader(
            user = postWithUser.user,
            secondaryText = postWithUser.timePassed,
            postType = postWithUser.post.postType,
            showPostType = true
        )
        PostBody(
            postWithUser = postWithUser,
            exoPlayer = exoPlayer,
            isVideoPlaying = isVideoPlaying,
            onPlayVideoClick = onPlayVideoClick
        )
        PostFooter(
            postWithUser = postWithUser,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun PostOnComments(
    postWithUser: PostWithUser,
    exoPlayer: ExoPlayer?,
    isVideoPlaying: Boolean,
    onPlayVideoClick : ()->Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        PostHeader(
            user = postWithUser.user,
            secondaryText = postWithUser.timePassed,
            postType = postWithUser.post.postType,
            showPostType = true
        )
        PostBody(
            postWithUser = postWithUser,
            exoPlayer = exoPlayer,
            isVideoPlaying = isVideoPlaying,
            onPlayVideoClick = onPlayVideoClick
        )
        CommentsFooter(text = "${postWithUser.post.numComments} comments")
    }
}

@Composable
fun Comment(
    commentWithUser: CommentWithUser
) {
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        PostHeader(user = commentWithUser.user, secondaryText = commentWithUser.comment.commentType, showPostType = false)
        CommentBody(comment = commentWithUser.comment.body)
        LikeButton(text = "Like", modifier = Modifier.padding(horizontal = standardPadding))
        Spacer(modifier = Modifier.height(8.dp))
    }
}
