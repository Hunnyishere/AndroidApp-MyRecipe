package com.wustlcse438sp20.myrecipe.Adapter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat

interface ItemTouchHelperAdapter {

   fun onItemDissmiss(position:Int){
   }
}