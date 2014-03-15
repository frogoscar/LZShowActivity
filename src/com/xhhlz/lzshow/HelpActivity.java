package com.xhhlz.lzshow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xhhlz.lzshow.R;
import com.xhhlz.lzshow.ui.PageIndicator;
import com.xhhlz.lzshow.ui.PagedAdapter;
import com.xhhlz.lzshow.ui.PagedView;
import com.xhhlz.lzshow.ui.PagedView.OnPagedViewChangeListener;

/**
 * Help Interface, uses PageView way. Use PageIndecator to switch between the pages
 * 
 * @author enming xie
 *
 */
public class HelpActivity extends Activity {
    private PageIndicator mPageIndicatorNext;
	private PageIndicator mPageIndicatorPrev;
	private PageIndicator mPageIndicatorOther;
	
	private static final int PAGE_COUNT = 6;
    private static final int PAGE_MAX_INDEX = PAGE_COUNT - 1;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.paged_view);
        final PagedView pagedView = (PagedView)findViewById(R.id.paged_view);
        pagedView.setOnPageChangeListener(mOnPagedViewChangedListener);
        pagedView.setAdapter(new PhotoSwipeAdapter());

        mPageIndicatorNext = (PageIndicator) findViewById(R.id.page_indicator_next);
        mPageIndicatorNext.setDotCount(PAGE_MAX_INDEX);
        mPageIndicatorNext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pagedView.smoothScrollToNext();
            }
        });

        mPageIndicatorPrev = (PageIndicator) findViewById(R.id.page_indicator_prev);
        mPageIndicatorPrev.setDotCount(PAGE_MAX_INDEX);
        mPageIndicatorPrev.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pagedView.smoothScrollToPrevious();
            }
        });
        
        mPageIndicatorOther = (PageIndicator) findViewById(R.id.page_indicator_other);
        mPageIndicatorOther.setDotCount(PAGE_COUNT);
        mPageIndicatorOther.getDotDrawable();
        setActivePage(pagedView.getCurrentPage());
        
    }
  
    private void setActivePage(int page) {
        mPageIndicatorOther.setActiveDot(page);
        mPageIndicatorNext.setActiveDot(PAGE_MAX_INDEX - page);
        mPageIndicatorPrev.setActiveDot(page);
    }
    
    private OnPagedViewChangeListener mOnPagedViewChangedListener = new OnPagedViewChangeListener() {

        public void onStopTracking(PagedView pagedView) {
        }

        public void onStartTracking(PagedView pagedView) {
        }

        public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
            setActivePage(newPage);
        }
    };
    
    private class PhotoSwipeAdapter extends PagedAdapter {
        
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.paged_view_item, parent, false);
            }

            if(position == 0)
            	((ImageView) convertView).setBackgroundResource(R.drawable.help1);
            else if(position == 1)
            	((ImageView) convertView).setBackgroundResource(R.drawable.help2);
            else if(position == 2)
            	((ImageView) convertView).setBackgroundResource(R.drawable.help3);
            else if(position == 3)
            	((ImageView) convertView).setBackgroundResource(R.drawable.help4);
            else if(position == 4)
            	((ImageView) convertView).setBackgroundResource(R.drawable.help5);
            else if(position == 5)
            	((ImageView) convertView).setBackgroundResource(R.drawable.help6);
            return convertView;
        }

    }
}