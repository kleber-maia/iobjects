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

## About
iObjects is all about reducing project's delivery time and ensuring product's high quality. It abstracts key pieces from all steps of the development cycle and makes the whole process a piece of cake. Even inexperienced developers can act as systems analysts, developers, testers and documenters, transforming needs in great, low budget solutions.

![](https://raw.github.com/kleber-maia/iobjects/master/README.img/1.png)

## Business objects
A long time ago, I read this book entitled "Theory of Responsibility" (in portuguese, Teoria da Responsabilidade, or something like that) which gave me a profound sense of object oriented design. It also made me rethink everything I had ever done, until I started to notice the "coincidences" between the problems and the solutions I had designed. At the end of that philosophical trip, I came up with these three design patterns which has been meeting 100% of my needs when designing information systems.

![](https://raw.github.com/kleber-maia/iobjects/master/README.img/2.png)

### Entity
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

## The story behind

I started developing iObjects in 2002 when I was decided to migrate from Win32/Delphi universe to the Web/Java/HTML/CSS/JavaScript stack. A few years before, in 1998, I had developed a pretty amazing framework with Delphi which was the foundation I had used to launch a best seller ERP product. That time, my company had closed several great contracts and we had grown from 2 to 20 developers in just a few months. The great news is that the ERP quality wasn't affected at all, even we having hired only interns. So, I was decided to replicate that success, but leaving Windows behind.

Between 2002 and 2008, this framework had a few different names and was used as foundation to develop dozens of information systems, to address several different problems, for several different companies. In 2008, it got its current name and shape. It's now made of 500K+ lines of codes and comments which, by the way, have been written exclusively by my two hands. As I can remember, the only third party component inside iObjects is the great [Highcharts](http://highcharts.com) JavaScript library, which I had not included in any of these previous countings.

Because this is a one man project, with a lot of other things to care about (like tight deadlines, team for manage, clients to attend, etc) you'll find a fair amount of silly code that, unfortunately, I had not had free time enough to go back and make it as elegant as I'd like to. The good news is that I care a lot to elegance, so you're going to find tons of well commented, well indented, well crafted code and creative solutions.

My only regret about iObjects is not having understood [Silvio Meira](https://twitter.com/srlm)'s advice to make it open source. While I'm writing these lines, I can see before my eyes names of great frameworks and I can't stop wondering: what if I had done that? I'm really sorry for being so late, but perhaps some software students may take advantage of any of my ideas and take it to a new level. Perhaps.
