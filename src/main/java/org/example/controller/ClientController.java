package org.example.controller;

import io.javalin.Javalin;
import org.example.model.Client;
import org.example.service.ClientService;
import org.example.view.ClientView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {

    private final ClientService service;

    public ClientController(Javalin app) {
        this.service = new ClientService();
        app.get("/clients",ctx ->
            ctx.html(ClientView.renderList(service.getClients())));
        app.get("/clients/new",ctx->
                ctx.html(ClientView.renderForm(new HashMap<>())));
        app.post("/clients", ctx -> {
            String name = ctx.formParam("name");
            String email = ctx.formParam("email");

            List<String> errors = new ArrayList<>();
            if (name == null || name.isBlank() || name == "") {
                errors.add("Nome não pode ser vazio");
            }
            if (email == null || !email.contains("@")) {
                errors.add("Email inválido");
            }
            if (!errors.isEmpty()) {
                ctx.html(ClientView.renderForm(Map.of(
                        "errors", errors,
                        "name", name,
                        "email", email
                )));
                return;
            }
            service.createClient(new Client(name, email));
            ctx.redirect("/clients");
        });
        app.get("/clients/edit/{id}",ctx->{
            int id = ctx.pathParamAsClass("id",Integer.class).get();
            Client client = service.getClientById(id);
            if(client != null){
                Map<String, Object> model = new  HashMap<>();
                model.put("id",client.getId());
                model.put("name",client.getName());
                model.put("email",client.getEmail());
                ctx.html(ClientView.renderForm(model));
            }else{
                ctx.status(404).result("Cliente não encontrado");
            }
        });

        app.post("clients/edit/{id}", ctx -> {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            String name = ctx.formParam("name");
            String email = ctx.formParam("email");

            List<String> errors = new ArrayList<>();
            if (name == null || name.isBlank() || name == "")  {
                errors.add("Nome não pode ser vazio");
            }
            if (email == null || !email.contains("@")) {
                errors.add("Email inválido");
            }
            if (!errors.isEmpty()) {
                ctx.html(ClientView.renderForm(Map.of(
                        "id", id,
                        "errors", errors,
                        "name", name,
                        "email", email
                )));
                return;
            }
            service.updateClient(new Client(id, name, email));
            ctx.redirect("/clients");
        });

        app.post("clients/delete/{id}",ctx->{
            int id = ctx.pathParamAsClass("id",Integer.class).get();
            service.deleteClientById(id);
            ctx.redirect("/clients");
        });
    }

    public ClientController(ClientService service) {
        this.service = service;
    }

    public List<Client> getClients(){
        return service.getClients();
    }

    public Client getClientById(int id){
        return service.getClientById(id);
    }

    public void createClient(Client client){
        service.createClient(client);
    }

    public void updateClient(Client client){
        service.updateClient(client);
    }


    public void deleteClientById(int id) {
        service.deleteClientById(id);
    }
}
