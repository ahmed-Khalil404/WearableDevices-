# Wearable Devices IoT CPS Project

This project focuses on building an IoT (Internet of Things) Cyber-Physical System (CPS) centered around wearable devices. The system comprises a network of interconnected devices, including smartwatches, smartphones, and cloud-based services, to collect, process, and visualize environmental and user data.

## Project Overview

The wearable devices within this project function as data collection nodes, equipped with sensors to monitor environmental conditions and user activities. The collected data is transmitted to a smartphone application via wireless communication protocols.

The smartphone application serves as an intermediary, receiving data from the wearable devices and forwarding it to a cloud-based database for storage and analysis. The cloud database stores the aggregated data, enabling real-time and historical data visualization using tools like Grafana.

## Key Components and Features

- **Smartwatch Data Collection**: Sensors on the smartwatch gather environmental data (e.g., temperature, humidity) and user metrics (e.g., heart rate, activity levels).
  
- **Smartphone Application**: Acts as a gateway to receive data from smartwatches and transmit it securely to the cloud database.
  
- **Cloud Database**: Stores incoming data for further analysis and visualization.
  
- **Visualization with Grafana**: Utilizes Grafana to create interactive dashboards and monitor the status and trends of collected data in real-time.


## Setup and Deployment

To deploy this project, follow these steps:

1. **Setup Smartwatch Firmware**:
   - Flash the firmware onto compatible smartwatches.
   - Configure sensor settings and data transmission protocols.

2. **Configure Smartphone Application**:
   - Install and configure the smartphone application to establish communication with the smartwatches.
   
3. **Set Up Cloud Database**:
   - Provision a cloud-based database (e.g., MySQL, MongoDB) with appropriate schemas to store incoming data.
   
4. **Integrate with Grafana**:
   - Connect Grafana to the cloud database to create dashboards and visualize data.

## Usage

1. **Data Collection**:
   - Wear the smartwatch to collect environmental and user data.
   
2. **Data Transmission**:
   - Open the smartphone application to establish a connection with the smartwatch and transmit data to the cloud.
   
3. **Visualization**:
   - Access Grafana dashboards to monitor and analyze the collected data.

## Conclusion

The Wearable Devices IoT CPS project demonstrates a scalable and efficient system for collecting, processing, and visualizing data from wearable devices. This architecture can be extended and customized for various IoT applications, providing valuable insights into environmental conditions and user health metrics.

