import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class Sidebar extends StatelessWidget {
  const Sidebar({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        children: [
          const DrawerHeader(child: Text('Menu')),
          ListTile(title: const Text('Home'), onTap: () => context.go('/home')),
          ListTile(
            title: const Text('Login'),
            onTap: () => context.go('/login'),
          ),
        ],
      ),
    );
  }
}
