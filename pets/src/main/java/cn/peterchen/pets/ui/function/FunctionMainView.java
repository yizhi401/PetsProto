package cn.peterchen.pets.ui.function;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.peterchen.pets.R;

/**
 * Created by peter on 15-2-5.
 */
public class FunctionMainView extends LinearLayout {

    private Context context;

    private Button loginBtn;

    private FunctionFragment fragment;

    public FunctionMainView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FunctionMainView(Context context, FunctionFragment fragment) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        init();
    }


    public FunctionMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FunctionMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.function_main_view, this);
        this.setOrientation(VERTICAL);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.changeViewType(FunctionFragment.VIEW_TYPE_LOGIN);
            }
        });
    }

}
