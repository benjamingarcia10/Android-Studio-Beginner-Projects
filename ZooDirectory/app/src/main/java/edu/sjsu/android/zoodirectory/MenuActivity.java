package edu.sjsu.android.zoodirectory;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.information:
                Intent myIntent = new Intent(this, ZooInformationActivity.class);
                this.startActivity(myIntent);
                return true;
            case R.id.action_uninstall:
                Intent deleteIntent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getPackageName())) ;
                this.startActivity(deleteIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
