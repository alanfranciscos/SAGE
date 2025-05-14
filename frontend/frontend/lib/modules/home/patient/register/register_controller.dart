import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class RegisterController {
  // Controladores para os dados pessoais
  final nomeController = TextEditingController();
  final cpfController = TextEditingController();
  final sexoController = TextEditingController();
  final dataNascimentoController = TextEditingController();

  // Controladores para o contato de emergência
  final contatoNomeController = TextEditingController();
  final contatoTelefoneController = TextEditingController();
  final contatoParentescoController = TextEditingController();

  // Controladores para residência
  final numeroControleController = TextEditingController();
  final numeroCasaController = TextEditingController();

  // Método para liberar memória dos controladores
  void dispose() {
    nomeController.dispose();
    cpfController.dispose();
    sexoController.dispose();
    dataNascimentoController.dispose();
    contatoNomeController.dispose();
    contatoTelefoneController.dispose();
    contatoParentescoController.dispose();
    numeroControleController.dispose();
    numeroCasaController.dispose();
  }

  // Método para converter os dados para JSON, agrupando tudo dentro de "paciente"
  Map<String, dynamic> patientData() {
    return {
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
        },
      }
    };
  }

  // Método para enviar o formulário para a API
  Future<bool> enviarFormulario() async {
    final url = Uri.parse('http://localhost:3000/pacientes'); // URL da API simulada

    // Dados para envio
    final data = patientData();

    try {
      // Realizando a requisição POST
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: json.encode(data), // Converte o Map para JSON
      );

      if (response.statusCode == 201) {
        print('Paciente cadastrado com sucesso!');
        print('Resposta: ${response.body}');
        return true;
      } else {
        print('Erro ao cadastrar paciente. Status: ${response.statusCode}');
        print('Resposta: ${response.body}');
        return false;
      }
    } catch (e) {
      print('Erro na requisição: $e');
      return false;
    }
  }
}
