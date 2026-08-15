# Implementation Plan - Add UI Previews for All Screens

This plan aims to add `@Preview` functions to all screen Composables that currently lack them. This will allow the developer to visualize UI changes instantly within Android Studio's Design tab.

## Proposed Changes

I will add `@Preview` functions to the following files, including necessary imports and mock data where required.

### Auth Screens
- **[MODIFY] [PhoneVerifyScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/auth/otp/PhoneVerifyScreen.kt)**: Add `PhoneVerifyScreenPreview`.
- **[MODIFY] [SignInScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/auth/signin/SignInScreen.kt)**: Add `SignInScreenPreview`.
- **[MODIFY] [SignUpScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/auth/signup/SignUpScreen.kt)**: Add `SignUpScreenPreview`.

### Home & Profile Screens
- **[MODIFY] [WalletScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/wallet/WalletScreen.kt)**: Add `WalletScreenPreview`.
- **[MODIFY] [PaymentMethodScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/payment/PaymentMethodScreen.kt)**: Add `PaymentMethodScreenPreview`.
- **[MODIFY] [ProfileScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/profile/ProfileScreen.kt)**: Add `ProfileScreenPreview`.
- **[MODIFY] [EditProfileScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/profile/EditProfileScreen.kt)**: Add `EditProfileScreenPreview`.
- **[MODIFY] [SettingsScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/settings/SettingsScreen.kt)**: Add `SettingsScreenPreview`.

### Management Screens
- **[MODIFY] [DrivingLicenseScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/document/DrivingLicenseScreen.kt)**: Add `DrivingLicenseScreenPreview`.
- **[MODIFY] [DocumentManagementScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/document/DocumentManagementScreen.kt)**: Add `DocumentManagementScreenPreview`.
- **[MODIFY] [AddVehicleScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/vehicle/AddVehicleScreen.kt)**: Add `AddVehicleScreenPreview`.
- **[MODIFY] [VehicleManagementScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/vehicle/VehicleManagementScreen.kt)**: Add `VehicleManagementScreenPreview`.

### Navigation Screen
- **[MODIFY] [PickupNavigationScreen.kt](file:///home/taher/AndroidStudioProjects/MyApplication3/app/src/main/java/com/newuperapp/Uper/ui/screens/home/navigation/PickupNavigationScreen.kt)**: Add `PickupNavigationScreenPreview` with mock `PickupNavigationState`.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure all `@Preview` functions are valid and don't break the build.

### Manual Verification
- Open the "Design" tab in Android Studio for several of the modified files to confirm the preview renders correctly.
