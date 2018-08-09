package gukbab1216.com.youthwelfare129;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import gukbab1216.com.youthwelfare129.adapter.AdapterImageSlider;
import gukbab1216.com.youthwelfare129.adapter.AdapterListAnimation;
import gukbab1216.com.youthwelfare129.adapter.CategoryGridViewAdapter;
import gukbab1216.com.youthwelfare129.data.DataGenerator;
import gukbab1216.com.youthwelfare129.databinding.ActivityMainBinding;
import gukbab1216.com.youthwelfare129.model.Image;
import gukbab1216.com.youthwelfare129.model.Welfare;
import gukbab1216.com.youthwelfare129.utils.ItemAnimation;
import gukbab1216.com.youthwelfare129.utils.TmpDatas;

import static gukbab1216.com.youthwelfare129.utils.TmpDatas.array_brief_place;
import static gukbab1216.com.youthwelfare129.utils.TmpDatas.array_image_place;
import static gukbab1216.com.youthwelfare129.utils.TmpDatas.array_title_place;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private ActivityMainBinding mBinding;
    private ActionBar mActionBar;
    private View mParentView;

    private AdapterImageSlider mAdapterImageSlider;
    private AdapterListAnimation mAdapterListAnimation;
    private List<Welfare> mItems = new ArrayList<>();
    private int mAnimationType = ItemAnimation.BOTTOM_UP;
    private Runnable mRunnable = null;
    private Handler mHandler = new Handler();

    private Typeface mTfLight;
    private Typeface mTfRegular;

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mParentView = findViewById(android.R.id.content);

        initToolbar();
        initNavigationMenu();
        initBanner();
        initCategory();
        initWelfareRankingList();
        initPieChart();
    }

    @Override
    public void onDestroy() {
        if (mRunnable != null) mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setTitle(getString(R.string.app_name));
    }

    private void initNavigationMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                mActionBar.setTitle(item.getTitle());
                mBinding.drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void initBanner() {
        mAdapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image>());

        final List<Image> items = new ArrayList<>();
        for (int i = 0; i < TmpDatas.array_image_place.length; i++) {
            Image obj = new Image();
            obj.image = array_image_place[i];
            obj.imageDrw = getResources().getDrawable(obj.image);
            obj.name = array_title_place[i];
            obj.brief = array_brief_place[i];
            items.add(obj);
        }

        mAdapterImageSlider.setItems(items);
        mBinding.drawerContent.pager.setAdapter(mAdapterImageSlider);

        // displaying selected image first
        mBinding.drawerContent.pager.setCurrentItem(0);
        addBottomDots(mBinding.drawerContent.layoutDots, mAdapterImageSlider.getCount(), 0);
        mBinding.drawerContent.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(mBinding.drawerContent.layoutDots, mAdapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(mAdapterImageSlider.getCount());
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                int pos = mBinding.drawerContent.pager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                mBinding.drawerContent.pager.setCurrentItem(pos);
                mHandler.postDelayed(mRunnable, 3000);
            }
        };
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void initWelfareRankingList() {
        mBinding.drawerContent.rankingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.drawerContent.rankingRecyclerview.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(mBinding.drawerContent.rankingRecyclerview, false);

        mItems = DataGenerator.getWelfareData(this);
        mItems.addAll(DataGenerator.getWelfareData(this));
        mItems.addAll(DataGenerator.getWelfareData(this));
        mItems.addAll(DataGenerator.getWelfareData(this));
        mItems.addAll(DataGenerator.getWelfareData(this));

        //set data and list adapter
        mAdapterListAnimation = new AdapterListAnimation(this, mItems, mAnimationType);
        mBinding.drawerContent.rankingRecyclerview.setAdapter(mAdapterListAnimation);
        // on item list clicked
        mAdapterListAnimation.setOnItemClickListener(new AdapterListAnimation.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Welfare obj, int position) {
                Snackbar.make(mParentView, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initPieChart() {
        mBinding.drawerContent.pieChart.setUsePercentValues(true);
        mBinding.drawerContent.pieChart.getDescription();
        mBinding.drawerContent.pieChart.setExtraOffsets(5, 10, 5, 5);
        mBinding.drawerContent.pieChart.setDragDecelerationFrictionCoef(0.95f);

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        mBinding.drawerContent.pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mBinding.drawerContent.pieChart.setCenterText(generateCenterSpannableText());

        mBinding.drawerContent.pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        mBinding.drawerContent.pieChart.setDrawHoleEnabled(true);
        mBinding.drawerContent.pieChart.setHoleColor(Color.WHITE);
        mBinding.drawerContent.pieChart.setTransparentCircleColor(Color.WHITE);
        mBinding.drawerContent.pieChart.setTransparentCircleAlpha(110);
        mBinding.drawerContent.pieChart.setHoleRadius(58f);
        mBinding.drawerContent.pieChart.setTransparentCircleRadius(61f);
        mBinding.drawerContent.pieChart.setDrawCenterText(true);
        mBinding.drawerContent.pieChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mBinding.drawerContent.pieChart.setRotationEnabled(true);
        mBinding.drawerContent.pieChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mBinding.drawerContent.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", xIndex: " + e.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });

        setData(4, 100);

        mBinding.drawerContent.pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mBinding.drawerContent.pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false);

        // entry label styling
        mBinding.drawerContent.pieChart.setEntryLabelColor(Color.WHITE);
        mBinding.drawerContent.pieChart.setEntryLabelTypeface(mTfRegular);
        mBinding.drawerContent.pieChart.setEntryLabelTextSize(12f);
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) (Math.random() * mult) + mult / 5, mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfRegular);
        mBinding.drawerContent.pieChart.setData(data);

        // undo all highlights
        mBinding.drawerContent.pieChart.highlightValues(null);

        mBinding.drawerContent.pieChart.invalidate();
    }

    private void initCategory() {
//
//        //GridView Start
//        GridView categoryGridView = findViewById(R.id.category_grid_view_image_text);
//
//        ArrayList<String> getStringList;
//        ArrayList<Integer> getIntList;
//
//        getStringList = new ArrayList<>();
//        getIntList = new ArrayList<>();
//
//        getStringList.add("구인구직");
//        getStringList.add("청년모임지원");
//        getStringList.add("학자금지원");
//        getStringList.add("가족관계");
//        getStringList.add("건강복지");
//        getStringList.add("공공지역");
//        getStringList.add("교육복지");
//        getStringList.add("노인복지");
//        getStringList.add("법률및권익보장");
//        getStringList.add("임신,출산복지");
//        getStringList.add("장애인복지");
//        getStringList.add("재테크");
//
//        getIntList.add(R.drawable.category_employ);
//        getIntList.add(R.drawable.category_community);
//        getIntList.add(R.drawable.category_studentloan);
//        getIntList.add(R.drawable.category_family);
//        getIntList.add(R.drawable.category_health);
//        getIntList.add(R.drawable.category_public);
//        getIntList.add(R.drawable.category_edu);
//        getIntList.add(R.drawable.category_oldage);
//        getIntList.add(R.drawable.category_law);
//        getIntList.add(R.drawable.category_parenting);
//        getIntList.add(R.drawable.category_disabled);
//        getIntList.add(R.drawable.category_investing);
//
//        String[] gridViewString;
//        int[] gridViewImageId;
//
//        gridViewString = new String[getStringList.size()];
//        gridViewImageId = new int[getStringList.size()];
//
//        for (int i = 0; i < getStringList.size(); i++) {
//            gridViewString[i] = getStringList.get(i);
//            gridViewImageId[i] = getIntList.get(i);
//        }
//
//        CategoryGridViewAdapter categoryGridViewAdapter;
//
//        categoryGridViewAdapter = new CategoryGridViewAdapter(getApplicationContext(), gridViewString, gridViewImageId);
//        categoryGridView.setAdapter(categoryGridViewAdapter);
//
//        categoryGridView.setOnItemClickListener((adapterView, view1, i, l) -> {
//            Intent intent = new Intent(getApplicationContext(), WelfareListActivity.class);
//            System.out.println(gridViewString[i]);
//            startActivity(intent);
//        });
    }
}
