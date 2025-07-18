# HealthMate: AI Health Assistant for Sri Lanka 🇱🇰

<p align="center">
  <img src="https://placehold.co/600x300/4A90E2/FFFFFF?text=HealthMate&font=raleway" alt="HealthMate App Banner">
</p>

<p align="center">
  <i>Your intelligent, bilingual health companion, designed to provide instant health information and support to the people of Sri Lanka.</i>
</p>

<p align="center">
    <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform: Android">
    <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language: Java">
    <img src="https://img.shields.io/badge/API-Google%20Gemini-blue.svg" alt="API: Google Gemini">
    <img src="https://img.shields.io/badge/License-MIT-lightgrey.svg" alt="License: MIT">
</p>

---

## 🌟 About HealthMate

HealthMate is an innovative Android application built to serve as a first-line health information tool for the Sri Lankan community. Recognizing the need for accessible and easy-to-understand health information, this app leverages the power of Google's Gemini AI to provide guidance on symptoms and identify medications. With full bilingual support for both **Sinhala** and **English**, HealthMate aims to bridge the gap between health consciousness and technology, empowering users to have more informed conversations with their healthcare providers.

This project was born from a vision to create a free, reliable, and user-friendly tool for everyone in Sri Lanka, built with passion and a commitment to public well-being.

## ✨ Key Features

* **🤖 AI Symptom Checker:** Describe your symptoms in plain English or Sinhala and get instant information about potential common conditions and general wellness advice.
* **📸 AI Medicine Identifier:** Not sure what a medicine is? Just take a picture of the tablet or box, and the AI will identify it and explain its common use.
* **🌐 Fully Bilingual:** Seamlessly switch between **English** and **Sinhala** at any time for a comfortable user experience. All AI responses are provided in your chosen language.
* **🎨 Elegant & Modern UI:** A clean, beautiful, and intuitive user interface inspired by modern design principles, ensuring the app is a pleasure to use.
* **💡 "Thinking" Animation:** A subtle animation provides visual feedback while the AI processes your request, making the app feel more responsive and alive.
* **⛑️ First-Aid Guide (Coming Soon):** A planned feature to provide crucial, offline first-aid information for common emergencies.

## 📸 Screenshots

| Home Screen  | AI Response | Language Selection |
| :---: |:---:|:---:|
| <img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/99fcd812-265c-426d-84eb-7f58776bffb5" /> | <img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/4e38e009-043e-4845-b7ec-cc8e1916af66" /> | <img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/566edb86-40c0-40e4-9d04-63c1df4c026a" /> |

## 🛠️ Tech Stack

* **Language:** Java
* **Platform:** Native Android (Android Studio)
* **AI Engine:** Google Gemini API (`gemini-2.0-flash`)
* **UI Components:** AndroidX, Material Design Components, View Binding, CardView
* **Networking:** OkHttp

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* [Android Studio](https://developer.android.com/studio) (latest version recommended)
* An Android device or emulator running API level 26 or higher

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/Viranya2006/HealthMate-Android.git
    ```

2.  **Open in Android Studio:**
    * Launch Android Studio.
    * Select `File -> Open` and navigate to the cloned project directory.
    * Let Gradle sync and build the project.

3.  **Add Your API Key (Crucial Step):**
    This project uses the Google Gemini API, which requires a free API key.

    * Get your free API key from [**Google AI Studio**](https://aistudio.google.com).
    * Open the file `app/src/main/java/com/example/healthmate/HomeFragment.java`.
    * Find the following line of code:
        ```java
        private final String apiKey = "YOUR_API_KEY_HERE";
        ```
    * **Replace** `"YOUR_API_KEY_HERE"` with your actual Gemini API key.

4.  **Run the App:**
    * Click the "Run" button (a green triangle) in Android Studio to build and install the app on your connected device or emulator.

## ⚠️ Important Disclaimer

**HealthMate is an informational tool and NOT a substitute for a real doctor.** The information provided by the AI is for general knowledge and guidance only. It is not a medical diagnosis. For any health concerns, accurate diagnosis, and treatment, **always consult a qualified medical professional.** The developers of this application are not liable for any decisions made based on the information provided by the app.

## 📝 Future Work

* Implement the offline **First-Aid Guide**.
* Add a **User Profile** section to save query history.
* Integrate a **Pill Reminder** feature.
* Develop a **Health & Wellness Articles** section.

## 📄 License

This project is licensed under the MIT License - see the `LICENSE.md` file for details.

---

<p align="center">
  Made with ❤️ in Sri Lanka
</p>
