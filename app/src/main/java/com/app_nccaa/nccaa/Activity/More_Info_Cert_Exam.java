package com.app_nccaa.nccaa.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app_nccaa.nccaa.R;

public class More_Info_Cert_Exam extends AppCompatActivity {

    private TextView headingtxt, textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_cert_exam);


        headingtxt = findViewById(R.id.headingtxt);
        textView4 = findViewById(R.id.textView4);
        
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String from = getIntent().getStringExtra("from");



        if (from.equals("cert")) {
            headingtxt.setText("Certification Exam");
            textView4.setText("Lorem ipsum dolor sit amet. Eos nullamaiores et blanditiisvoluptatemestsintmodi sed nulla autem estmolestiaeomniseumexercitationemfacerenamporroearum. Eaharumdignissimos et aliquamreprehenderit et doloribusipsam et impeditquod in eiusveritatis. Est odit vitae sed saepe facilis aut rerum aperiam qui obcaecatiasperioresestexpeditasimilique. Qui fugit provident utvoluptate rerum et consequaturvelit." +
                    "Et dignissimosmollitia qui doloresmodi qui corruptirepellatutdebitis error sed explicaboquisutinventoreconsequaturestreiciendis corporis. Et voluptas fugit rem dolores vitae sit accusantiumblanditiis et ametteneturest illum adipisci. Hic architectoadipisci quo voluptasaliquidutenim facilis utdistinctiovero qui exercitationemmollitia. Est eiusperferendis 33 omnismaiores et temporibustempora sit quiaiure." +
                    "Autevenietgalisum sit omnisnulla et repellat rerum et inciduntveritatis! Et magnisint in reprehenderit vitae et ratione sunt et laudantiumullam et doloresipsamodiodeserunt.");

        } else if (from.equals("cdq")){
            headingtxt.setText("CDQ Exam");
            textView4.setText("Lorem ipsum dolor sit amet. Eos nullamaiores et blanditiisvoluptatemestsintmodi sed nulla autem estmolestiaeomniseumexercitationemfacerenamporroearum. Eaharumdignissimos et aliquamreprehenderit et doloribusipsam et impeditquod in eiusveritatis. Est odit vitae sed saepe facilis aut rerum aperiam qui obcaecatiasperioresestexpeditasimilique. Qui fugit provident utvoluptate rerum et consequaturvelit." +
                    "Et dignissimosmollitia qui doloresmodi qui corruptirepellatutdebitis error sed explicaboquisutinventoreconsequaturestreiciendis corporis. Et voluptas fugit rem dolores vitae sit accusantiumblanditiis et ametteneturest illum adipisci. Hic architectoadipisci quo voluptasaliquidutenim facilis utdistinctiovero qui exercitationemmollitia. Est eiusperferendis 33 omnismaiores et temporibustempora sit quiaiure." +
                    "Autevenietgalisum sit omnisnulla et repellat rerum et inciduntveritatis! Et magnisint in reprehenderit vitae et ratione sunt et laudantiumullam et doloresipsamodiodeserunt.");

        } else if (from.equals("cme")){
            headingtxt.setText("CME Submissions");
            textView4.setText("Lorem ipsum dolor sit amet. Eos nullamaiores et blanditiisvoluptatemestsintmodi sed nulla autem estmolestiaeomniseumexercitationemfacerenamporroearum. Eaharumdignissimos et aliquamreprehenderit et doloribusipsam et impeditquod in eiusveritatis. Est odit vitae sed saepe facilis aut rerum aperiam qui obcaecatiasperioresestexpeditasimilique. Qui fugit provident utvoluptate rerum et consequaturvelit." +
                    "Et dignissimosmollitia qui doloresmodi qui corruptirepellatutdebitis error sed explicaboquisutinventoreconsequaturestreiciendis corporis. Et voluptas fugit rem dolores vitae sit accusantiumblanditiis et ametteneturest illum adipisci. Hic architectoadipisci quo voluptasaliquidutenim facilis utdistinctiovero qui exercitationemmollitia. Est eiusperferendis 33 omnismaiores et temporibustempora sit quiaiure." +
                    "Autevenietgalisum sit omnisnulla et repellat rerum et inciduntveritatis! Et magnisint in reprehenderit vitae et ratione sunt et laudantiumullam et doloresipsamodiodeserunt.");

        } else if (from.equals("state_licencing")){
            headingtxt.setText("State Licensing Form");
            textView4.setText("Lorem ipsum dolor sit amet. Eos nullamaiores et blanditiisvoluptatemestsintmodi sed nulla autem estmolestiaeomniseumexercitationemfacerenamporroearum. Eaharumdignissimos et aliquamreprehenderit et doloribusipsam et impeditquod in eiusveritatis. Est odit vitae sed saepe facilis aut rerum aperiam qui obcaecatiasperioresestexpeditasimilique. Qui fugit provident utvoluptate rerum et consequaturvelit." +
                    "Et dignissimosmollitia qui doloresmodi qui corruptirepellatutdebitis error sed explicaboquisutinventoreconsequaturestreiciendis corporis. Et voluptas fugit rem dolores vitae sit accusantiumblanditiis et ametteneturest illum adipisci. Hic architectoadipisci quo voluptasaliquidutenim facilis utdistinctiovero qui exercitationemmollitia. Est eiusperferendis 33 omnismaiores et temporibustempora sit quiaiure." +
                    "Autevenietgalisum sit omnisnulla et repellat rerum et inciduntveritatis! Et magnisint in reprehenderit vitae et ratione sunt et laudantiumullam et doloresipsamodiodeserunt.");

        } else if (from.equals("add_cme")){
            headingtxt.setText("CME Submissions");
            textView4.setText("Lorem ipsum dolor sit amet. Eos nullamaiores et blanditiisvoluptatemestsintmodi sed nulla autem estmolestiaeomniseumexercitationemfacerenamporroearum. Eaharumdignissimos et aliquamreprehenderit et doloribusipsam et impeditquod in eiusveritatis. Est odit vitae sed saepe facilis aut rerum aperiam qui obcaecatiasperioresestexpeditasimilique. Qui fugit provident utvoluptate rerum et consequaturvelit." +
                    "Et dignissimosmollitia qui doloresmodi qui corruptirepellatutdebitis error sed explicaboquisutinventoreconsequaturestreiciendis corporis. Et voluptas fugit rem dolores vitae sit accusantiumblanditiis et ametteneturest illum adipisci. Hic architectoadipisci quo voluptasaliquidutenim facilis utdistinctiovero qui exercitationemmollitia. Est eiusperferendis 33 omnismaiores et temporibustempora sit quiaiure." +
                    "Autevenietgalisum sit omnisnulla et repellat rerum et inciduntveritatis! Et magnisint in reprehenderit vitae et ratione sunt et laudantiumullam et doloresipsamodiodeserunt.");

        }


    }


}