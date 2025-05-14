import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:frontend/model/patient_model.dart';

class PacienteUpdateController with ChangeNotifier {
  PatientModel? paciente;
  bool isLoading = true;
  bool hasError = false;

  final TextEditingController nomeController = TextEditingController();
  final TextEditingController cpfController = TextEditingController();
  final TextEditingController sexoController = TextEditingController();
  final TextEditingController dataNascimentoController = TextEditingController();
  final TextEditingController contatoNomeController = TextEditingController();
  final TextEditingController contatoTelefoneController = TextEditingController();
  final TextEditingController contatoParentescoController = TextEditingController();
  final TextEditingController numeroControleController = TextEditingController();
  final TextEditingController numeroCasaController = TextEditingController();

  Future<void> fetchPacienteById(String id) async {
    final url = Uri.parse('http://localhost:3000/pacientes/$id'); 

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        paciente = PatientModel.fromJson(data);

        // Preencher os campos do formulário
        nomeController.text = paciente!.nome;
        cpfController.text = paciente!.cpf;
        sexoController.text = paciente!.sexo;
        dataNascimentoController.text = paciente!.dataNascimento;
        contatoNomeController.text = paciente!.contatoNome;
        contatoTelefoneController.text = paciente!.contatoTelefone;
        contatoParentescoController.text = paciente!.contatoParentesco;
        numeroControleController.text = paciente!.numeroControle;
        numeroCasaController.text = paciente!.numeroCasa;

        hasError = false;
      } else {
        hasError = true;
      }
    } catch (error) {
      hasError = true;
      print('Erro ao buscar paciente: $error');
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  Future<void> updatePaciente(String id) async {
    final url = Uri.parse('http://localhost:3000/pacientes/$id');

    final Map<String, dynamic> data = {
      "paciente": {
        "dadosPessoais": {
          "nome": nomeController.text,
          "cpf": cpfController.text,
          "sexo": sexoController.text,
          "dataNascimento": dataNascimentoController.text,
        },
        "contatoEmergencia": {
          "nome": contatoNomeController.text,
          "telefone": contatoTelefoneController.text,
          "parentesco": contatoParentescoController.text,
        },
        "residencia": {
          "numeroControle": numeroControleController.text,
          "numeroCasa": numeroCasaController.text,
        }
      }
    };

    try {
      final response = await http.put(
        url,
        headers: {'Content-Type': 'application/json'},
        body: json.encode(data),
      );

      if (response.statusCode == 200) {
        print("Paciente atualizado com sucesso!");
      } else {
        print("Erro ao atualizar paciente.");
      }
    } catch (error) {
      print("Erro na atualização: $error");
    }
  }
}
