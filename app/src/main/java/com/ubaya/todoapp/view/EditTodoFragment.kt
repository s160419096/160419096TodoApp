package com.ubaya.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.ubaya.todoapp.R
import com.ubaya.todoapp.viewmodel.DetailTodoViewModel
import com.ubaya.todoapp.viewmodel.ListTodoViewModel

class EditTodoFragment : Fragment() {
    private lateinit var viewModel: DetailTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        val txtJudulTodo = view.findViewById<TextView>(R.id.txtJudulTodo)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val radioGroupPriority = view.findViewById<RadioGroup>(R.id.radioGroupPriority)
        val txtTitle = view.findViewById<TextInputEditText>(R.id.txtTitle)
        val txtNotes = view.findViewById<TextInputEditText>(R.id.txtNotes)

        txtJudulTodo.text = "Edit Todo"
        btnAdd.text = "Save Changes"

        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)

        btnAdd.setOnClickListener {
            val radio =
                view.findViewById<RadioButton>(radioGroupPriority.checkedRadioButtonId)
            viewModel.update(txtTitle.text.toString(), txtNotes.text.toString(),
                radio.tag.toString().toInt(), uuid)
            Toast.makeText(view.context, "Todo updated", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(it).popBackStack()
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            val txtTitle = view?.findViewById<TextInputEditText>(R.id.txtTitle)
            val txtNotes = view?.findViewById<TextInputEditText>(R.id.txtNotes)
            val radioLow = view?.findViewById<RadioButton>(R.id.radioLow)
            val radioMedium = view?.findViewById<RadioButton>(R.id.radioMedium)
            val radioHigh = view?.findViewById<RadioButton>(R.id.radioHigh)

            txtTitle?.setText(it.title)
            txtNotes?.setText(it.notes)

            when (it.priority) {
                1 -> radioLow?.isChecked = true
                2 -> radioMedium?.isChecked = true
                else -> radioHigh?.isChecked = true
            }
        })
    }
}