package com.oliver.gymondo.exercise.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.oliver.gymondo.R
import com.oliver.gymondo.database.models.ExerciseImage
import com.oliver.gymondo.database.models.ModelExercise

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, exercise: ModelExercise?) {
    if (!exercise!!.images.isNullOrEmpty()) {
        Glide.with(view)
            .load(exercise.images!![0].image)
            .placeholder(R.drawable.ic_image_black_24dp)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(view)
    }
}

@BindingAdapter("imageFromDetails")
fun bindImage(view: ImageView, images: ExerciseImage?) {
    if (images != null) {
        Glide.with(view)
            .load(images.image)
            .placeholder(R.drawable.ic_image_black_24dp)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(view)
    }
}

@BindingAdapter("textFromExercises")
fun bindTextFromExercises(view: TextView, exercise: ModelExercise?) {
    exercise?.let {
        val equipmentNames: MutableList<String> = mutableListOf()
        val musclesNames: MutableList<String> = mutableListOf()
        for (i in exercise.equipment!!.indices) {
            equipmentNames.add(exercise.equipment[i].name!!)
        }
        val equipment: String = musclesOrEquipmentToString(equipmentNames)!!
        for (i in exercise.muscles!!.indices) {
            musclesNames.add(exercise.muscles[i].name)
        }
        val muscles: String = musclesOrEquipmentToString(musclesNames)!!
        view.text = String.format(
            "name: %s\n" +
                    "category: %s\n" +
                    "equipment: %s\n" +
                    "muscles: %s\n",
            exercise.name,
            exercise.category?.category_name,
            equipment,
            muscles
        )
    }
}

@BindingAdapter("textFromExerciseDetail")
fun bindTextFromExerciseDetail(view: TextView, exercise: ModelExercise?) {
    exercise?.let {
        val equipmentNames: MutableList<String> = mutableListOf()
        val musclesNames: MutableList<String> = mutableListOf()
        for (i in exercise.equipment!!.indices) {
            equipmentNames.add(exercise.equipment[i].name!!)
        }
        val equipment: String = musclesOrEquipmentToString(equipmentNames)!!
        for (i in exercise.muscles!!.indices) {
            musclesNames.add(exercise.muscles[i].name)
        }
        val muscles: String = musclesOrEquipmentToString(musclesNames)!!
        view.text = String.format(
            "name: %s\n" +
                    "category: %s\n" +
                    "description: %s\n" +
                    "equipment: %s\n" +
                    "muscles: %s\n",
            exercise.name,
            exercise.category!!.category_name,
            exercise.description,
            equipment,
            muscles
        )

    }
}

fun musclesOrEquipmentToString(array: List<String>?): String? {
    var result = ""
    return if (array == null || array.isEmpty()) {
        "no information"
    } else {
        for (str in array) {
            result += "$str, "
        }
        result
    }
}


