import 'package:flutter/material.dart';
import 'package:frontend/shared/themes/app_theme.dart';
import 'package:go_router/go_router.dart';

class UserCard extends StatelessWidget {
  final String imagePath;
  final String name;
  final String number;
  final String status;

  const UserCard({
    super.key,
    required this.imagePath,
    required this.name,
    required this.number,
    required this.status,
  });

  Color _getBackgroundColor() {
    switch (status) {
      case '1':
        return AppTheme.alert;
      case '2':
        return AppTheme.danger;
      default:
        return Colors.white;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      color: _getBackgroundColor(),
      elevation: 3,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            CircleAvatar(backgroundImage: AssetImage(imagePath), radius: 30),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    name,
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                  Text(number, style: const TextStyle(fontSize: 14)),
                ],
              ),
            ),
            ElevatedButton(
              onPressed: () {
                context.go('/patient-update');
              },
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                backgroundColor: AppTheme.tertiaryColor,
                foregroundColor: AppTheme.textDarkColor,
              ),
              child: const Text('Detalhes'),
            ),
          ],
        ),
      ),
    );
  }
}
