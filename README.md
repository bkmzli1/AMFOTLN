AMFOTLN 
==========
# СОДЕРЖАНИЕ
1. [ВВЕДЕНИЕ](#ВВЕДЕНИЕ)
1. [ПЛАНЫ](#ПЛАНЫ)
1. [УСТАНОВКА ПРОЭКТА](#УСТАНОВКА-ПРОЭКТА)
1. [КОМПИЛЯЦИЯ ПРОЭКТА](#КОМПИЛЯЦИЯ-ПРОЭКТА)


# ВВЕДЕНИЕ

AMFOTLN - это кодовое название прокат *Automatically move files on the local network* который предназначен для копирования 
и отправки файлов по локальной сети на другие пк.   
Данный проэкт разрабатывается специально для *НТИ НИЯУ МИФИ*
        
[:arrow_up:СОДЕРЖАНИЕ](#СОДЕРЖАНИЕ)
[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#table-of-contents)
# ПЛАНЫ
:white_check_mark: Это уже сделано :negative_squared_cross_mark: Я не буду это делать :black_square_button: в планах    
        
            
:white_check_mark: Добовление удаление IP адресов    
:white_check_mark: Выбор на какие IP адреса отправлять файлы  
:white_check_mark: Логирование проекта  
:white_check_mark: Предложение выбора отправления файла\папки   
:black_square_button: Предоставить выбор функции настроек отправки xcopy    
:black_square_button: Удаление Файлов\папок     
:black_square_button: Настройки управления                  
:black_square_button: Установщик            
:black_square_button: Управление ПК  (*перезагрузка, выключение(через n время),и тп*)       
:black_square_button: Поддержка старых платформ (*xp*)      
:black_square_button: Удаление приложений    
:black_square_button: Установка приложений      
:negative_squared_cross_mark: Настройка ПК  
:negative_squared_cross_mark: удалённое управление мыши,клавиатуры ПК   
        
[:arrow_up:СОДЕРЖАНИЕ](#СОДЕРЖАНИЕ)
[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#table-of-contents)
# УСТАНОВКА ПРОЭКТА
## 3.1 Подготовка
1. Установить [***Git***](https://git-scm.com/)
1. Устанавливаем [***JDK***](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
1. Установите [***IntelliJ IDEA*** ](https://www.jetbrains.com/idea/)
на этом подготовка закончилась
## 3.2 Импорт проекта
1. Нажимаем на Configure ->  Structure for new projects
1. затем new JDK (рисунок 4)
1. в открывшимся окне выбераем путь к jdk уменя он `C:\Program Files\Java\jdk1.8.0_231` и нажимаем ок
1. Нажмите Get from Version Control (Рисунок 1)
1. На сайте нажимаем Clone or download и копируем ссылку [***AMFOTLN***](https://github.com/bkmzli1/AMFOTLN) (Рисунок 2)
1. Скопированную ссылку вставляем в поле URL и нажимаем clone (Рисунок 3)
1. При клонировании могут возникать вопросы выбираем нужный как вы считает это индивидуально
1. После чего должна пойти настройка проекта в случаи если этого не произошло то открываем файл build.gradle и зажимаем сочетание ```Ctrl+Shift+F10```
1. Открываем файл по пути ```src/main/java/ru/bkmz/tehSistem/Main``` и зажимаем сочетание ```Ctrl+Shift+F10```
1. Поздравляем проект успешно импортирован  
        
```Рисунок 1```         
![Alt-текст](https://github.com/bkmzli1/AMFOTLN/blob/master/img/1.png?raw=true "Рисунок 1")           
```Рисунок 2```     
![Alt-текст](https://github.com/bkmzli1/AMFOTLN/blob/master/img/2.png?raw=true "Рисунок 2")              
```Рисунок 3```
![Alt-текст](https://github.com/bkmzli1/AMFOTLN/blob/master/img/3.png?raw=true "Рисунок 3")                 
```Рисунок 4```
![Alt-текст](https://github.com/bkmzli1/AMFOTLN/blob/master/img/4.png?raw=true "Рисунок 4")   
```Рисунок 5```
![Alt-текст](https://github.com/bkmzli1/AMFOTLN/blob/master/img/5.png?raw=true "Рисунок 5")          
        
[:arrow_up:СОДЕРЖАНИЕ](#СОДЕРЖАНИЕ)
[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#table-of-contents)
#КОМПИЛЯЦИЯ ПРОЭКТА
1. открываем build.gradle и редоктируем следёющие строчки
```gradle
def versions = '1.1.7.6' \\версия
def name = 'AMFOTLN reliz '\\ имя проэкта
def main = 'ru.bkmz.tehSistem.Main'\\путь к стартовому классу 
//находим следующие строчки и вписываем сворё имя или имя компании
companyName = "BkzmLitel"
copyright = "BkzmLitel"

```
1. в правой части есть вкладка gradle открываем её
1. открываем Tasks/Launch4j
1. запускаем launch4j(рисунок 4)    
1. начнётся компиляция. после завершения exe  файл появится в ```build\launch4j\```         
```Рисунок 4```         
![Alt-текст](https://github.com/bkmzli1/AMFOTLN/blob/master/img/4.png?raw=true "Рисунок 4")           
