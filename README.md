# iObjects
iObjects is a framework written in Java/HTML/CSS/JavaScript for developing web and mobile information systems. Some of its key features are: 
- 200+ classes, tools and UI components; 
- 3 code generators; 
- Dozens of design patterns; 
- Component based model with business objects and plugins; 
- Multi-database connection and transaction manager; 
- Microsoft Office like user interface; 
- Smartphone user interface; 
- Replaceable security service; 
- User color schemes; 
- Integrated help system and release notes;
- Dozens of other exciting features. 

You can take a look at the [iObjects CRM](https://github.com/kleber-maia/iobjects-crm) and [iObjects Security Service](https://github.com/kleber-maia/iobjects-security-service) projects to see some screen shots of a final product developed on iObjects.

## About
iObjects is all about reducing project's delivery time and ensuring product's high quality. It abstracts key pieces from all steps of the development cycle and makes the whole process a piece of cake. Even inexperienced developers can act as systems analysts, developers, testers and documenters, transforming needs in great, low budget solutions.

![](https://raw.github.com/kleber-maia/iobjects/master/README.img/1.png)

Although the source code is really well documented, all the comments (and the user interface as well) were published as is: in brazilian portuguese. I apologize for any inconvenience, but I'm afraid I'll not have spare time enough to work on any kind of translation.

## Running
1. Download/obtain the project.
2. Create the database: there is a .sql file on the root of this project which contains all the needed table structure, plus a few sample records. Although the script was generated from a PostgreSQL database, it should be easy to migrate it to another RDMS of your choice.
3. Open **iobjects/web.work/connections/default.dbc** and configure it to access your recently created database.
4. If you are using Netbeans 8+, just Run the project (F6). If you are using another IDE, it may require some configuration.
5. You should see the login screen in your default web browser.

The following users will be automatically created on the first run. Additionally, you'll be able to insert any users and roles that you need by using the [Security Service](https://github.com/kleber-maia/iobjects-security-service) module's interface.
- Username: **@Super Usuário**, Password: **superusuario**
- Username: **Administrador**, Password: **administrador**
- Username: **Convidado**, Password: **convidado**

#### Technical requisites
- Java 1.6 or higher
- Netbeans 8.0 or higher.
- PostgreSQL 8.0 or higher or any RDMS of your choice.
 
#### Compatibility
All you need to take full advantage of the application is a modern web browser, like: Safari 7 or higher, Chrome 432 or higher =), Internet Explorer 9 or higher.

## Business objects
A long time ago, I read this book entitled "Theory of Responsibility" (in portuguese, Teoria da Responsabilidade, or something like that) which gave me a profound sense of object oriented design. It also made me rethink everything I had ever done, until I started to notice the "coincidences" between the problems and the solutions I had designed. At the end of that philosophical trip, I came up with these three design patterns which has been meeting 100% of my needs when designing information systems.

![](https://raw.github.com/kleber-maia/iobjects/master/README.img/2.png)

#### Entity
The Entity is, by far, the only "complex" design pattern. Considering the nature of information systems, where users spend 80% of the time inserting records, the Entity has to cover several different use cases. So, which are the Entity's responsibilities and characteristics?

- Each database table must have its related Entity class. Why? Because...
- Only that Entity and that Entity only is responsible for manipulating its related database table's records.
- It contains field descriptors representing each of the table field's attributes, like: field name, type, size, caption, help description, format, mask and constraints.
- It may contains lookups to retrieve other table's field values by using foreign keys.
- It has a delete method to...delete a record, of course.
- It has a validate method to...validate a record before inserting or updating, of course.
- It has an insert method to...insert a record, of course.
- It has an update method to...update a record, of course.
- It has a findByPrimaryKey method to...return a record by using its primary key.
- It has a searchByFilter method to...return one or more records by using custom search criterias.
- It counts with several reusable controls to make it easy to display and edit records.

There is a particular use case that, sometimes, creates some confusion: master-detail relationships. If you just assume that the master and the detail records may be inserted in different transactions (non atomic) there is no complexity at all in this case: it's just a matter of screen design and both database tables may be managed independently by its related Entity class.

#### Process
The Process is where the magic of the simplicity happens. At this point, I'm pretty sure you got what the Entity is all about: manage a single database table. So, how about the requirements where we need to deal with two or more database tables? We are talking about the Process, of course. How it works?

- It contains user params to allow the user to select options, enter data or whatever you need.
- It contains a method execute in which you:
  - receive those params;
  - instantiate all needed Entities;
  - run your business logic;
  - commit the changes to the database.

You may have noticed that all the performed operations are atomic. Additionally, that particular business logic you had just implemented must never be implemented elsewhere.

On the UI side, the Process counts with the Wizard: my web implementation of the famous Microsoft Windows wizards where the user is guided through a few steps, answering questions and/or selecting options, until the process may finally execute.

#### Report
The Report design pattern is the easiest to understand and implement. Every requirement that only shows data and/or information to the user will be implemented as a Report. Let's see its characteristics:

- It contains user params to allow the user to select filter options or whatever you need.
- It must contain a method for each section of the report in which you:
  - receive those params;
  - run your pretty amazing SQL script;
  - return a Resultset with your querie's result.

The rest of the magic happens on the screen, with beautiful charts and zebra grids. iObjects offers a dozen of controls to make even more easy to loop into the Resultset's records, format, align and show its data.

## Extensions
Many years ago, I got a job on this company which was migrating its ERP software from MS-DOS (pretty jurassic, ahn?) to Windows using Delphi. Turns out that the software was so big that, at some point, Windows couldn't even run it anymore. The lead engineers had this "brilliant" idea of dismembering the software in several executables. I left the company at the end of that day. =)

Extensions are one of the most exciting things I have ever implemented. First time in Delphi and after that in Java. Although the platforms are totally different, the concept is the same: having the possibility of dynamically loading modules containing business logic and its UI.

![](https://raw.github.com/kleber-maia/iobjects/master/README.img/3.png)

iObjects was designed in such way that it is, indeed, the application. Instead of creating an application on iObjects, you'll create extensions containing business objects. Is up to you how many extensions you will have and which business objects each one will have inside.

If I could give you only one advice, I'd tell you: keep things simple. If you plan to unplug or exchange a particular extension, be careful with how other classes depend on its classes. The ExtensionManager class provides an easy way to check if an extensions is present or not. Additionally, you may want to create a traceability matrix to keep track on the dependencies.

## Actions
We have discussed a lot about the business logic and you may be wondering: how about the UI? Now that you know about extensions and business objects, I can reveal the magic behind the Action. Each business object (Entity, Process or Report) has an ActionList which may contain none, one or more Actions. Each Action points to a JSP and contains attributes to help the user easily identify its function.

![](https://raw.github.com/kleber-maia/iobjects/master/README.img/4.png)

When you create an Action:
- The application's Ribbon is automatically updated with a new tab, section and button to reflect the Action's Module, Category and Caption attributes.
- The objects and commands tree view in the Security Service module automatically shows your Action with its Commands so the administrators can allow/deny access to the users.
- The objects tree in the online help automatically shows your Action with its related attributes and FAQ objects.

## The story behind

I started developing iObjects in 2002 when I was decided to migrate from Win32/Delphi universe to the Web/Java/HTML/CSS/JavaScript stack. A few years before, 1998-2001, I had developed a pretty amazing framework with Delphi which was the foundation I had used to launch a best seller ERP product. That time, my company had closed several great contracts and we had grown from 2 to 20 developers in just a few months. The great news is that the ERP quality wasn't affected at all, even we having hired only interns. So, I was decided to replicate that success, but leaving Windows behind.

Between 2002 and 2008, this framework had a few different names and was used as foundation to develop dozens of information systems, to address several different problems, for several different companies. In 2008, it got its current name and shape. It's now made of 500K+ lines of codes and comments which, by the way, have been written exclusively by my two hands. As I can remember, the only third party component inside iObjects is the great [Highcharts](http://highcharts.com) JavaScript library, which I had not included in any of these previous countings.

Because this is a one man project, with a lot of other things to care about (like tight deadlines, team for manage, clients to attend, etc) you'll find a fair amount of silly code that, unfortunately, I had not had free time enough to go back and make it as elegant as I'd like to. The good news is that I care a lot to elegance, so you're going to find tons of well commented, well indented, well crafted code and creative solutions.

My only regret about iObjects is not having understood [Silvio Meira](https://twitter.com/srlm)'s advice to make it open source. While I'm writing these lines, I can see before my eyes names of great frameworks and I can't stop wondering: what if I had done that? I'm really sorry for being so late, but perhaps some software students may take advantage of any of my ideas and take it to a new level. Perhaps.
