package ru.absolutelee.vknewsclient.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.absolutelee.vknewsclient.R
import ru.absolutelee.vknewsclient.domain.entities.FeedPost
import ru.absolutelee.vknewsclient.domain.entities.StatisticItem
import ru.absolutelee.vknewsclient.domain.entities.StatisticType
import ru.absolutelee.vknewsclient.presentation.formatStatisticCount

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onSharesItemClick: (StatisticItem) -> Unit,
    onCommentsItemClick: (StatisticItem) -> Unit,
    onLikesItemClick: (StatisticItem) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        PostCardHeader(feedPost)
        PostCardContent(feedPost)
        PostCardStatistic(
            statistics = feedPost.statistics,
            onSharesItemClick = onSharesItemClick,
            onCommentsItemClick = onCommentsItemClick,
            onLikesItemClick = onLikesItemClick,
            isLiked = feedPost.isLiked
        )
    }
}

@Composable
private fun PostCardContent(
    feedPost: FeedPost
) {
    Text(
        text = feedPost.contentText,
        modifier = Modifier.padding(8.dp)
    )
    AsyncImage(
        model = feedPost.contentImageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentScale = ContentScale.FillWidth

    )
}

@Composable
private fun PostCardStatistic(
    statistics: List<StatisticItem>,
    onSharesItemClick: (StatisticItem) -> Unit,
    onCommentsItemClick: (StatisticItem) -> Unit,
    onLikesItemClick: (StatisticItem) -> Unit,
    isLiked: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
        StatisticsIconAndCount(
            value = viewsItem.count,
            iconResId = R.drawable.ic_views_count,
        )

        Spacer(modifier = Modifier.weight(1f))

        val sharesItem = statistics.getItemByType(StatisticType.SHARES)
        StatisticsIconAndCount(
            value = sharesItem.count,
            iconResId = R.drawable.ic_share,
            onClick = {
                onSharesItemClick(sharesItem)
            }
        )

        val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
        StatisticsIconAndCount(
            value = commentsItem.count,
            iconResId = R.drawable.ic_comment,
            onClick = {
                onCommentsItemClick(commentsItem)
            }
        )

        val likesItem = statistics.getItemByType(StatisticType.LIKES)
        StatisticsIconAndCount(
            tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSecondary,
            value = likesItem.count,
            iconResId = if (isLiked) R.drawable.ic_like_set else R.drawable.ic_like,
            onClick = {
                onLikesItemClick(likesItem)
            }
        )
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type } ?: throw IllegalStateException()
}

@Composable
private fun PostCardHeader(feedPost: FeedPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = feedPost.communityThumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)

        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(
                text = feedPost.communityName, fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feedPost.publicationData,
            )

        }
        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
    }
}

@Composable
private fun StatisticsIconAndCount(
    tint: Color = MaterialTheme.colorScheme.onSecondary,
    value: Int,
    iconResId: Int,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick == null) {
        Modifier
    } else {
        Modifier
            .clickable {
                onClick()
            }
    }

    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value.formatStatisticCount())
    }

}


