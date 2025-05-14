import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:frontend/model/patient_model.dart';

class PacienteDetailsController with ChangeNotifier {
  PatientModel? paciente;
  bool isLoading = true;
  bool hasError = false;

  Future<void> fetchPacienteById(String id) async {
    final url = Uri.parse('http://localhost:3000/pacientes/$id'); // URL da API

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        paciente = PatientModel.fromJson(data);
        hasError = false;
      } else {
        hasError = true;
        throw Exception('Erro ao buscar paciente. Status: ${response.statusCode}');
      }
    } catch (error) {
      hasError = true;
      print('Erro ao buscar paciente: $error');
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }
}
