package com.example.xyzreader.adapters;

import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nestor on 2/9/18.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    public static ArticleItemListener mArticleItemListener;
    private final String TAG = this.getClass().getName().toString();
    private List<Article> mArticles = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

    public ArticleAdapter(ArticleItemListener articleItemListener) {
        mArticleItemListener = articleItemListener;
    }


    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        final ArticleViewHolder vh = new ArticleViewHolder(view);
        return vh;

    }

    private Date parsePublishedDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.titleView.setText(mArticles.get(position).getTITLE());
        Date publishedDate = parsePublishedDate(mArticles.get(position).getPUBLISHED_DATE());
        if (!publishedDate.before(START_OF_EPOCH.getTime())) {

            holder.dateView.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()));
//                                + "<br/>" + " by "
//                                + mCursor.getString(ArticleLoader.Query.AUTHOR)));
            Log.d(TAG, " time is not " +
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString());
        } else {
            holder.dateView.setText(Html.fromHtml(
                    outputFormat.format(publishedDate)));
//                        + "<br/>" + " by "
//                        + mCursor.getString(ArticleLoader.Query.AUTHOR)))
            Log.d(TAG, " time is  " + outputFormat.format(publishedDate));

        }
        holder.itemView.setTag(position);
        holder.loadImage(mArticles.get(position).getTHUMB_URL());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.thumbnailView.setTransitionName(mArticles.get(position).get_ID());
        }
        holder.authorView.setText(mArticles.get(position).getAUTHOR());
        // holder.subtitleView.setText(Html.fromHtml(mArticles.get(position).getBODY()));

    }

    @Override
    public int getItemCount() {
        if (mArticles != null) return mArticles.size();
        return 0;
    }


    //Reload  new articles after refresh
    public void swapArticlesData(@NonNull List<Article> articles) {
        mArticles = new ArrayList<>(articles);
        for (Article article : mArticles) {
            article.reduceBodyLength();
        }

        notifyDataSetChanged();
    }

    public interface ArticleItemListener {
        void onArticleClick(@NonNull int position, @NonNull ImageView imageView);

    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // @BindView(R.id.thumbnail) DynamicHeightNetworkImageView thumbnailView;
        @BindView(R.id.thumbnail)
        ImageView thumbnailView;
        @BindView(R.id.article_title)
        TextView titleView;
        //        @BindView(R.id.article_subtitle)
//        TextView subtitleView;
        @BindView(R.id.article_author)
        TextView authorView;
        @BindView(R.id.article_date)
        TextView dateView;

        public ArticleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            setFonts(view);
        }

        public void loadImage(@NonNull String imageUrl) {
            Picasso.with(itemView.getContext()).load(imageUrl).into(thumbnailView);

        }

        private void setFonts(View view) {
            Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(),
                    "Rosario-Regular.ttf");
            titleView.setTypeface(tf);
            //subtitleView.setTypeface(tf);
            authorView.setTypeface(tf);
            dateView.setTypeface(tf);

        }

        @Override
        public void onClick(View view) {
            mArticleItemListener.onArticleClick((int) itemView.getTag(), thumbnailView);
        }

    }
}
