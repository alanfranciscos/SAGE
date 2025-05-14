import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:frontend/model/patient_model.dart';
import 'package:http/http.dart' as http;

class PatientController with ChangeNotifier {
  final List<PatientModel> _patients = [];
  bool _isLoading = false;
  String? _error;

  List<PatientModel> get patients => _patients;
  bool get isLoading => _isLoading;
  String? get error => _error;

  Future<void> fetchPatients() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final response = await http.get(Uri.parse('http://localhost:3000/pacientes'));

      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        _patients.clear();
        _patients.addAll(data.map((e) => PatientModel.fromJson(e)).toList());
      } else {
        _error = 'Erro ${response.statusCode} ao buscar dados.';
      }
    } catch (e) {
      _error = 'Erro ao buscar pacientes: $e';
    }

    _isLoading = false;
    notifyListeners();
  }
}
