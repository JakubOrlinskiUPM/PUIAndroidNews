package com.example.puiandroidnews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends BaseAdapter implements Filterable {
    MainActivity activity;
    private List<Article> data = null;
    private List<Article>filteredData = null;
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

        TextView articleTitleLabel = convertView.findViewById(R.id.articleTitleLabel);
        articleTitleLabel.setText(filteredData.get(position).getTitleText());

        TextView articleCategoryLabel = convertView.findViewById(R.id.articleCategoryLabel);
        articleCategoryLabel.setText(filteredData.get(position).getCategory());

        convertView.setOnClickListener(v -> {
            activity.routeToArticle(filteredData.get(position));
        });

        return convertView;
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
