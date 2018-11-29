package com.jpcami.tads.xsearch;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.jpcami.tads.xsearch.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Remover o TitleBar da Splash Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //Aviso de inicialização
        Toast.makeText(this, "Aguarde a inicialização do aplicativo.", Toast.LENGTH_SHORT).show();
        Handler h = new Handler();

        //Executa o código na thread principal após 3 segundos usando o método postDelayed
        h.postDelayed(new Runnable() {
        public void run() {
            //Abre a activity principal
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            //Destroí a Splash para que o usuário não tenha como voltar nela
            finish();
            }
        }, 3000);

    }
}
