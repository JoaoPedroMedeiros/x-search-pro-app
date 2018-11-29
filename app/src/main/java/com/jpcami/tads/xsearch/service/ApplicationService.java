package com.jpcami.tads.xsearch.service;

import com.jpcami.tads.xsearch.entity.Mutant;
import com.jpcami.tads.xsearch.entity.Skill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApplicationService {

    private static final String domain = "10.0.2.2:8080";

    public List<String> changeMutant(Mutant mutant) throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/mutants/", domain));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("PUT");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        try {
            JSONObject mut = new JSONObject();
            mut.put("id", mutant.getId());
            mut.put("name", mutant.getName());

            JSONArray arr = new JSONArray();

            for (Skill skill: mutant.getSkills()) {
                JSONObject sk = new JSONObject();
                sk.put("id", skill.getId());
                sk.put("name", skill.getName());
                arr.put(sk);
            }

            mut.put("skills", arr);

            System.out.println(mut.toString());
            conn.getOutputStream().write(mut.toString().getBytes());
        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ao montar o corpo da requisição :(", e);
        } catch (JSONException e) {
            throw new IOException("Ops! Houve um erro ao montar o corpo da requisição :(", e);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
            List<String> messages = new ArrayList<>();
            return messages;
        }
        catch (IOException e) {
            if (conn.getResponseCode() == 412) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getErrorStream())))) {
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
                    JSONObject obj = new JSONObject(response);

                    JSONArray arr = obj.getJSONArray("errorMessages");

                    List<String> messages = new ArrayList<>();

                    messages.add(obj.getString("message"));

                    for (int i = 0; i < arr.length(); i++) {
                        messages.add(arr.getString(i));
                    }

                    return messages;
                } catch (JSONException e1) {
                    throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
                }
            }
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }

    public List<String> newMutant(Mutant mutant) throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/mutants/", domain));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("POST");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        try {
            JSONObject mut = new JSONObject();
            if (mutant.getId() != null)
                mut.put("id", mutant.getId());

            mut.put("name", mutant.getName());

            JSONArray arr = new JSONArray();

            for (Skill skill: mutant.getSkills()) {
                JSONObject sk = new JSONObject();
                sk.put("id", skill.getId());
                sk.put("name", skill.getName());
                arr.put(sk);
            }

            mut.put("skills", arr);

            System.out.println(mut.toString());
            conn.getOutputStream().write(mut.toString().getBytes());
        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ao montar o corpo da requisição :(", e);
        } catch (JSONException e) {
            throw new IOException("Ops! Houve um erro ao montar o corpo da requisição :(", e);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
            List<String> messages = new ArrayList<>();
            messages.add(new JSONObject(response).getString("id"));
            return messages;
        }
        catch (IOException e) {
            if (conn.getResponseCode() == 412) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getErrorStream())))) {
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
                    JSONObject obj = new JSONObject(response);

                    JSONArray arr = obj.getJSONArray("errorMessages");

                    List<String> messages = new ArrayList<>();

                    messages.add(obj.getString("message"));

                    for (int i = 0; i < arr.length(); i++) {
                        messages.add(arr.getString(i));
                    }

                    return messages;
                } catch (JSONException e1) {
                    throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
                }
            }
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        } catch (JSONException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }

    public List<String> newSkill(String name) throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/skills/", domain));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("POST");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        try {
            conn.getOutputStream().write(String.format("{\"name\": \"%s\"}", name).getBytes());
        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ao montar o corpo da requisição :(", e);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
            List<String> messages = new ArrayList<>();
            messages.add(new JSONObject(response).getString("id"));
            return messages;
        }
        catch (IOException e) {
            if (conn.getResponseCode() == 412) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getErrorStream())))) {
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
                    JSONObject obj = new JSONObject(response);

                    JSONArray arr = obj.getJSONArray("errorMessages");

                    List<String> messages = new ArrayList<>();

                    messages.add(obj.getString("message"));

                    for (int i = 0; i < arr.length(); i++) {
                        messages.add(arr.getString(i));
                    }

                    return messages;
                } catch (JSONException e1) {
                    throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
                }
            }
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        } catch (JSONException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }

    public List<Skill> getSkills() throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/skills", domain));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("GET");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(false);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;

            JSONArray arr = new JSONArray(response);

            List<Skill> skills = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject item = arr.getJSONObject(i);
                Skill skill = new Skill();
                skill.setId(item.getInt("id"));
                skill.setName(item.getString("name"));
                skills.add(skill);
            }

            return skills;

        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        } catch (JSONException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }

    public List<String> delete(Integer id) throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/mutants/%s", domain, id));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("DELETE");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }

        conn.setDoInput(true);
        conn.setDoOutput(false);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return new ArrayList<>();
        }
        catch (IOException e) {
            if (conn.getResponseCode() == 412) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getErrorStream())))) {
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
                    JSONObject obj = new JSONObject(response);

                    JSONArray arr = obj.getJSONArray("errorMessages");

                    List<String> messages = new ArrayList<>();

                    messages.add(obj.getString("message"));

                    for (int i = 0; i < arr.length(); i++) {
                        messages.add(arr.getString(i));
                    }

                    return messages;
                } catch (JSONException e1) {
                    throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
                }
            }
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }

    public List<Mutant> getMutants(String filter) throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/mutants/%s", domain, filter != null ? URLEncoder.encode(filter, "UTF-8")  : ""));
        }
        catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("GET");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(false);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;
            System.out.println(new JSONArray(response));

            JSONArray arr = new JSONArray(response);

            List<Mutant> mutants = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject item = arr.getJSONObject(i);
                Mutant mutant = new Mutant();
                mutant.setId(item.getInt("id"));
                mutant.setName(item.getString("name"));

                List<Skill> skills = new ArrayList<>();

                if (item.getJSONArray("skills") != null) {
                    for (int j = 0; j < item.getJSONArray("skills").length(); j++) {
                        Skill skill = new Skill();
                        skill.setId(item.getJSONArray("skills").getJSONObject(j).getInt("id"));
                        skill.setName(item.getJSONArray("skills").getJSONObject(j).getString("name"));
                        skills.add(skill);
                    }
                }

                mutant.setSkills(skills);
                mutants.add(mutant);
            }

            return mutants;
        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
        catch (JSONException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }

    public Boolean autenticate(String user, String password) throws IOException {
        URL url;
        try {
            url = new URL(String.format("http://%s/users/", domain));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            throw new IOException("Ops! Não foi possível conectar com o servidor :(", e);
        }

        try {
            conn.setRequestMethod("POST");
        }
        catch (ProtocolException e) {
            throw new IOException("Ops! Houve um erro ao montar a requisição :(", e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        try {
            conn.getOutputStream().write(String.format("{\"password\":\"%s\",\"login\":\"%s\"}", password, user).getBytes());
        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ao montar o corpo da requisição :(", e);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String response = sb.toString() != null && !sb.toString().isEmpty() ? sb.toString() : null;

            try {
                JSONObject result = new JSONObject(response);
                return result.getBoolean("authenticated");
            }
            catch (JSONException e) {
                throw new IOException("Ops! Retorno veio em um formato inesperado :(", e);
            }
        }
        catch (IOException e) {
            throw new IOException("Ops! Houve um erro ler o retorno da requisição :(", e);
        }
    }
}
