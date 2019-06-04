package example.com.homomorphiccrypto.Main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.com.homomorphiccrypto.R;
import example.com.homomorphiccrypto.Screen.ResultScreen;
import example.com.homomorphiccrypto.Screen.VotingScreen;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    public BottomNavigationView navigationView;

    @BindView(R.id.container)
    FrameLayout container;

    private Unbinder unbinder;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_voting:
                    setPage(new VotingScreen());
                    return true;
                case R.id.nav_result:
                    setPage(new ResultScreen());
                    return true;
            }
            return false;
        }
    };

    private void setPage(Fragment f){
        fragmentManager.beginTransaction().replace(R.id.container, f)
                .addToBackStack(null).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);
        unbinder = ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        setPage(new VotingScreen());
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
