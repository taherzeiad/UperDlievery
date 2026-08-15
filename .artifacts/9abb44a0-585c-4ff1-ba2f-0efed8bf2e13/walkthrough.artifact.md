# Walkthrough - UI Previews for All Screens

I have added `@Preview` functions to all major screens in the application. This allows you to see the UI and any changes you make in real-time within the Android Studio Design tab.

## Changes Made

### 1. Auth Screens
- Added previews for [PhoneVerifyScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/auth/otp/PhoneVerifyScreen.kt), [SignInScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/auth/signin/SignInScreen.kt), and [SignUpScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/auth/signup/SignUpScreen.kt).

### 2. Home & Profile Screens
- Added/Updated previews for [WalletScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/wallet/WalletScreen.kt), [PaymentMethodScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/payment/PaymentMethodScreen.kt), [ProfileScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/profile/ProfileScreen.kt), [EditProfileScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/profile/EditProfileScreen.kt), and [SettingsScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/settings/SettingsScreen.kt).
- Refactored some screens to separate the "State-aware" Composable from the "Stateless" Composable, making them easier to preview with mock data.

### 3. Management Screens
- Added previews for [DrivingLicenseScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/document/DrivingLicenseScreen.kt), [DocumentManagementScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/document/DocumentManagementScreen.kt), [AddVehicleScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/vehicle/AddVehicleScreen.kt), and [VehicleManagementScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/vehicle/VehicleManagementScreen.kt).

### 4. Navigation Screen
- Added a comprehensive preview for [PickupNavigationScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/navigation/PickupNavigationScreen.kt) including a simulated navigation state with route points and steps.

## How to use
1. Open any of the screen files mentioned above.
2. Click on the **Design** or **Split** tab in the top right corner of the editor.
3. You will see the visual representation of the screen. You can change texts or colors in the code, and the preview will update automatically (after a short build).

## Verification Results

### Automated Tests
- Successfully ran `./gradlew assembleDebug`. All previews are correctly implemented and do not conflict with each other or the main application code.
