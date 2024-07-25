package com.example.myshoppinglistapp

import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocal
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)


@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(25.dp)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {
                //ShoppingListItem(it, {}, {})
                    item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuatity ->
                        sItems = sItems.map {
                            it.copy(isEditing = false)
                        }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuatity

                        }
                    })
                } else {
                    ShoppingListItem(item = item,
                        onEditClick = {
                            sItems = sItems.map {
                                it.copy(isEditing = it.id == item.id)
                            }
                        },
                        onDeleteClick = {
                            sItems = sItems - item
                        }
                    )
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            modifier = Modifier.border(
                border = BorderStroke(2.dp, Color(0xFF6200EA)), // Purple border color
                shape = RoundedCornerShape(10)
            ),
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )

                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = ""
                        }
                    }) {
                        Text(text = "Add")

                    }
                    Button(onClick = {
                        showDialog = false

                    }) {
                        Text(text = "Cancel")

                    }
                }
            },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center // Center the title horizontally
                ) {
                    Text(text = "Add Shopping Item")
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        label = { Text(text = "Item Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        singleLine = true,
                        label = { Text(text = "Quantity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )


    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(20)) // Light grey background color

            .border(BorderStroke(2.dp, Color(0xFF6200EA)), RoundedCornerShape(20)), // Purple border and rounded corners
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically // Center items vertically within the row
    ) {
        Column {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp) // Padding around the text field
                    .border(
                        border = BorderStroke(1.dp, Color(0xFF6200EA)), // Purple border color
                        shape = RoundedCornerShape(10)
                    )
            ) {
                BasicTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    singleLine = true,
                    modifier = Modifier
                        .padding(5.dp) // Padding inside the box, creating space between text and border
                )
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp) // Padding around the text field
                    .border(
                        border = BorderStroke(1.dp, Color(0xFF6200EA)), // Purple border color
                        shape = RoundedCornerShape(10)
                    )
            ) {
                BasicTextField(
                    value = editedQuantity,
                    onValueChange = { editedQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .padding(5.dp) // Padding inside the box, creating space between text and border
                )
            }
        }
        Button(
            onClick = {
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            },
            modifier = Modifier
                .align(Alignment.CenterVertically) // Center the button vertically within the row
                .padding(8.dp) // Add padding if needed
        ) {
            Text("Save", color = Color.White) // White text color
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF6200EA)), // Purple border color
                shape = RoundedCornerShape(20)
            )
            .background(Color(0xFFF0F0F0)) // Light grey background color
            .padding(8.dp), // Internal padding
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Center vertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(8.dp),
            color = Color.Black,
            fontSize = 20.sp
        )
        Text(
            text = "Quantity: ${item.quantity}",
            modifier = Modifier.padding(8.dp),
            color = Color.Black,
            fontSize = 20.sp
        )
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Button",
                    tint = Color(0xFF6200EA)
                ) // Purple icon
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Button",
                    tint = Color.Red
                ) // Red icon
            }
        }
    }
}

