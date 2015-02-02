# Ensembles de Julie en Hadoop

Projet Hadoop qui calcul les ensembles de Julia

# Authors
Stéephane Ferreira
Marwan Ghanem

#Utilisation
A lire entièrement avant d’utiliser

Hadoop 2.6.0 doit être déjà installé et lancer sur votre machine

Placez vous à l’aide de votre terminal dans le dossier où vous voulez récupérer les
fichiers d’entré / sortie

Lancez la commande suivante : hadoop jar <jarPath> main.Main <in> <out>
jarPath : chemin ou se situe le jar
in : dossier dans le hdfs où va être le fichier d’entré 
+ nom du fichier d’entré en local
out : dossier dans hdfs ou vont être crée les fichiers de sortie

Attention, les fichier d’entré avec le meme nom seront écrasé.
Les fichiers de sortie ne seront pas écrasé, si ilx existe déjà , aucun fichier ne
sera récupéré.



# Screen

![alt tag](/screen.png)