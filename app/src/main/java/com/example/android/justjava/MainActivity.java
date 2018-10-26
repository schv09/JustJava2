package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        //Getting the checked state from each checkbox
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        //Calculating price
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        //Getting the name that was entered
        EditText nameEditTextField = (EditText) findViewById(R.id.name);
        String name = nameEditTextField.getText().toString();

        //Passing in parameters to the method that will create the order summary string
        String orderSummary = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        //Passing in parameters to the method that will make an e-mail intent
        sendEmail(name, orderSummary);
    }

    /**
     * Calculates the price of the order.
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether pr not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        //Base price of 1 cup of coffee
        int basePrice = 5;

        //Add $1 if the user wants whipped cream
        if(hasWhippedCream) {
            basePrice += 1;
        }

        //Add $2 if the user wants chocolate
        if(hasChocolate) {
            basePrice += 2;
        }

        //Calculate total price by multiplying by total quantity
        return basePrice * quantity;
    }

    /**
     * Creates a summary of the order
     *
     * @param price total price of the order
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether pr not the user wants chocolate topping
     * @param name the customer's name
     * @return order summary
     */

    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String name) {
        String orderSummary = "Name: " + name +
                              "\nAdd whipped cream? " + hasWhippedCream +
                              "\nAdd chocolate? " + hasChocolate +
                              "\nQuantity: " + quantity +
                              "\nTotal: $" + price +
                              "\nThank you!";
        return orderSummary;
    }

    /**
     * This method is called when the plus button is clicked.
     * It increases the quantity of cups of coffee ordered by one.
     */
    public void increment(View view) {

        if(quantity == 100) {
            //Show error message as toast
            Toast.makeText(this, "You can't order more than 100 coffees!", Toast.LENGTH_LONG).show();

            //exit method early
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     * It decreases the quantity of cups of coffee ordered by one.
     */
    public void decrement(View view) {
        if(quantity == 1) {
            //Show error message as toast
            Toast.makeText(this, "You can't order less than 1 coffee!", Toast.LENGTH_LONG).show();

            //exit method early
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void sendEmail(String name, String orderSummary) {

        //Create subject for e-mail using name of customer
        String subject = "JustJava order for " + name;

        //Create body text for email with summary of order
        String body = orderSummary;

        //Create intent (Check Common Intents Guide)
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); //Add subject line
        intent.putExtra(Intent.EXTRA_TEXT, body); //Add body text

        //Don't run activity if there is no app that can handle it
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
