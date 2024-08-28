import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ExchangeClient {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/10dddc71174b91bd29d6d0b5";

    public static void main(String[]args){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/10dddc71174b91bd29d6d0b5/latest/ARS"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

                String baseCurrency = jsonObject.get("base_code").getAsString();
                JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

                Scanner scanner = new Scanner(System.in);
                boolean exit = false;

                while (!exit) {
                    System.out.println("Seleccione la moneda a la que desea convertir desde ARS:");
                    System.out.println("1. USD (Dólar estadounidense)");
                    System.out.println("2. BOB (Boliviano boliviano)");
                    System.out.println("3. BRL (Real brasileño)");
                    System.out.println("4. CLP (Peso chileno)");
                    System.out.println("5. COP (Peso colombiano)");
                    System.out.println("6. Salir");

                    int opcionElegida = scanner.nextInt();

                    if (opcionElegida == 6){
                        exit = true;
                        System.out.println("Saliendo de app");;
                    }else {
                        System.out.println("Ingrese monto a cambiar en ARS:");
                        double amountInARS = scanner.nextDouble();

                        switch (opcionElegida){
                            case 1:
                                convertAndPrint(conversionRates, "USD", amountInARS);
                                break;
                            case 2:
                                convertAndPrint(conversionRates, "BOB", amountInARS);
                                break;
                            case 3:
                                convertAndPrint(conversionRates, "BRL", amountInARS);
                                break;
                            case 4:
                                convertAndPrint(conversionRates, "CLP", amountInARS);
                                break;
                            case 5:
                                convertAndPrint(conversionRates, "COP", amountInARS);
                                break;
                            default:
                                System.out.println("Opción no válida. Intente de nuevo.");
                        }


                    }

                }
                scanner.close();

            }else{
                System.out.println("Error: " + response.statusCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void convertAndPrint(JsonObject conversionRates, String currencyCode, double amountInARS) {
        if (conversionRates.has(currencyCode)) {
            double rate = conversionRates.get(currencyCode).getAsDouble();
            double convertedAmount = amountInARS * rate;
            System.out.println(amountInARS + " ARS = " + convertedAmount + " " + currencyCode);
        } else {
            System.out.println("No se encontró la tasa de cambio para " + currencyCode);
        }
    }
}