import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:frontend/shared/themes/app_theme.dart';

class Sidebar extends StatefulWidget {
  const Sidebar({super.key});

  @override

  _SidebarState createState() => _SidebarState();
}

class _SidebarState extends State<Sidebar> {
  @override
  Widget build(BuildContext context) {
    final currentRoute =
        GoRouter.of(context).routerDelegate.currentConfiguration;

    final String location = currentRoute.last.matchedLocation;

    return Drawer(
      backgroundColor: AppTheme.primaryColor,
      shape: const RoundedRectangleBorder(
      borderRadius: BorderRadius.zero,
  ),
      child: Column(
        children: [
          Container(
            height: 160,
            color: AppTheme.primaryColor,
            alignment: Alignment.center,
            child: Image.asset(
              'assets/images/light_logo.png',
              width: 120,
              height: 120,
              fit: BoxFit.contain,
            ),
          ),
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                _buildListTile(
                  icon: Icons.home,
                  title: 'Home',
                  route: '/home',
                  currentRoute: location,
                  context: context,
                ),

                _buildListTile(
                  icon: Icons.person,
                  title: 'Perfil',
                  route: '/profile',
                  currentRoute: location,
                  context: context,
                ),
                _buildListTile(
                  icon: Icons.settings,
                  title: 'Configurações',
                  route: '/settings',
                  currentRoute: location,
                  context: context,
                ),
              ],
            ),
          ),
          
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            child: SizedBox(
              width: 100,
              child: ElevatedButton.icon(
                icon: const Icon(Icons.logout),
                label: const Text('Sair'),
                style: ElevatedButton.styleFrom(
                  minimumSize: const Size.fromHeight(38),
                  
                  backgroundColor: AppTheme.tertiaryColor,
              
                  foregroundColor: AppTheme.textDarkColor,
                ),
                onPressed: () {
                  context.go('/login');
                },
              ),
            ),
          ),
          const SizedBox(height: 14),
        ],
      ),
    );
  }

  ListTile _buildListTile({
    required IconData icon,
    required String title,
    required String route,
    required String currentRoute,
    required BuildContext context,
  }) {
    final isSelected = currentRoute == route;

    return ListTile(
      leading: Icon(
        icon,
        color: isSelected ? Colors.white : AppTheme.textLightColor,
      ),
      title: Text(
        title,
        style: TextStyle(
          fontSize: 14,
          color: isSelected ? Colors.white : AppTheme.textLightColor,
          fontWeight: FontWeight.w500,
        ),
      ),
      selected: isSelected,
      selectedTileColor: AppTheme.secondaryColor,
      tileColor: Colors.transparent,
      onTap: () {
        if (!isSelected) {
          setState(() {
            context.go(route);
          });
        }
      },
    );
  }
}
