package com.example.puiandroidnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puiandroidnews.exceptions.ServerCommunicationError;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends BaseAdapter implements Filterable {
    MainActivity activity;
    private List<Article> data = null;
    private List<Article> filteredData = null;
    private final ItemFilter mFilter = new ItemFilter();


    public ArticleAdapter(MainActivity activity) {
        this.activity = activity;
    }

    public void setData(List<Article> data) {
        this.data = data;
        this.filteredData = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return filteredData != null ? filteredData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return filteredData != null ? filteredData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.article_list_item, null);
        }

        Article article = filteredData.get(position);
        TextView articleTitleLabel = convertView.findViewById(R.id.articleTitleLabel);
        articleTitleLabel.setText(article.getTitleText());

        TextView articleAbstractLabel = convertView.findViewById(R.id.articleAbstractLabel);
        articleAbstractLabel.setText(Html.fromHtml(article.getAbstractText(), Html.FROM_HTML_MODE_COMPACT));

        TextView articleCategoryLabel = convertView.findViewById(R.id.articleCategoryLabel);
        articleCategoryLabel.setText(article.getCategory());

        ImageView articleImageView = convertView.findViewById(R.id.articleImageView);
        Bitmap bitmap = null;
        try {
            Image img = article.getImage();
            if (img != null) {
                String str = img.getImage();
                if (str != null) {
                    bitmap = stringToBitMap(str);
                }
            }
        } catch (ServerCommunicationError serverCommunicationError) {
            System.out.println("oh no");
        }
        if (bitmap == null) {
            articleImageView.setImageResource(R.drawable.fallback_article_image);
        } else {
            articleImageView.setImageBitmap(bitmap);
        }

        convertView.setOnClickListener(v -> {
            activity.routeToArticle(filteredData.get(position));
        });

        return convertView;
    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();


            final ArrayList<Article> nlist = new ArrayList<Article>(data.size());

            if (filterString.equals("all")) {
                nlist.addAll(data);
            } else {
                for (Article article : data) {
                    if (article.getCategory().toLowerCase().equals(filterString)) {
                        nlist.add(article);
                    }
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<Article>) results.values;
            notifyDataSetChanged();
        }

    }
}
