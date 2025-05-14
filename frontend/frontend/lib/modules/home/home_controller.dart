import 'dart:convert';
import 'package:frontend/model/patient_model.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';

class PacienteController with ChangeNotifier {
  List<PatientModel> pacientes = [];

  //   Future<void> fetchPacientes() async {
  //     final url = Uri.parse('http://localhost:3000/pacientes'); // URL do JSON Server

  //     try {
  //       final response = await http.get(url);

  //       if (response.statusCode == 200) {
  //         final List<dynamic> data = json.decode(response.body);
  //         pacientes = data.map((json) => PatientModel.fromJson(json)).toList();
  //         notifyListeners(); // Atualiza os ouvintes do estado
  //       } else {
  //         throw Exception('Erro ao buscar pacientes. Status ${response.statusCode}');
  //       }
  //     } catch (error) {
  //       print('Erro ao buscar pacientes: $error');
  //     }
  //   }
  // }

  Future<void> fetchPacientes() async {
    final url = Uri.parse('http://localhost:3000/pacientes');

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        pacientes = data.map((json) => PatientModel.fromJson(json)).toList();

        print("Pacientes carregados: ${pacientes.length}");

        notifyListeners();
      } else {
        throw Exception(
          'Erro ao buscar pacientes. Status ${response.statusCode}',
        );
      }
    } catch (error) {
      print('Erro ao buscar pacientes: $error');
    }
  }
}
