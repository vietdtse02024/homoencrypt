package example.com.homomorphiccrypto.Main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.R;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.placeholder)
    ImageView placeholder;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.description)
    TextView description;

    Unbinder unbinder;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_detail);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("Chi tiết");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Object temp = getIntent().getSerializableExtra("person");
        if(temp != null){
            person = (Person) temp;

            note.setText(person.getAddress());
            name.setText(person.getName());
            description.setText(person.getDescription());

            Glide.with(this)
                    .load(person.getImgUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .centerCrop()
                    .into(placeholder);
        }else{
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
