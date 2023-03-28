<div>
  <h3 align="center"> Resort Management System </h3>

  <p align="center">
    A Resort Management System, coded in Java.
    <br />
  </p>
  <p align="center">
    Completed: November 2021
    <br />
  </p>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#screenshots">Screenshots</a>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## Screenshots

|    **Login**    |     **Dashboard**    |
:---------------------------:|:----------------------------:|
![1-login](/screenshots/login.png) | ![2-dashboard](/screenshots/dashboard.png)  |

|    **Reservations**    |     **Guests**    |
:---------------------------:|:----------------------------:|
![3-reservations](/screenshots/reservations.png) | ![4-guests](/screenshots/guests.png)  |

|    **Rooms**    |     **Admin Tools**    |
:----------------:|:-------------------------:
![5-rooms](/screenshots/rooms.png) | ![6-admin_settings](/screenshots/admin_settings.png)  |

## About The Project

This application is a Resort Management System (RMS). It is a desktop application coded in Java, and the database is MySQL. 
This RMS allows the user to:
* make, manage, and track guest reservations
* collect guest information
* add and reserve rooms/cottages for individuals or groups
* view and generate reports (occupancy, guest demographics)


### Built With

* Java


<!-- GETTING STARTED -->
## Getting Started

To get this app running on your machine, simply clone the repository and ensure the following prerequisites are all installed.

### Prerequisites

The following programs were used for the development of this project:
* Java
* XAMPP Control Panel v3.2.4 (for MySQL database)


### Installation

1. Clone the repo
   ```
   git clone https://github.com/GituMbugua/RMS.git
   ```
2. Change the following lines
   ```
   String database = "jdbc:mysql://localhost:3306/<database_name>";
   con = DriverManager.getConnection(database, "<user-name>", "<password>");
   ```
3. Import the database
   

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.


<!-- CONTACT -->
## Contact

Gitu Mbugua - gmbugua38@gmail.com

Project Link: [https://github.com/GituMbugua/RMS](https://github.com/GituMbugua/RMS)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
