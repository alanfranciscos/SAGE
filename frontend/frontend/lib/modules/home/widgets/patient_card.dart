import 'package:flutter/material.dart';
import 'package:frontend/shared/themes/app_theme.dart';

class UserCard extends StatelessWidget {
  final String imagePath;
  final String name;
  final String number;

  const UserCard({
    super.key,
    required this.imagePath,
    required this.name,
    required this.number,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
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
              onPressed: () {},
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
