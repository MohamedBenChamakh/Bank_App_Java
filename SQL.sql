/*==============================================================*/
/* Nom de SGBD :  MySQL 5.0                                     */
/* Date de création :  26/02/2021 20:32:14                      */
/*==============================================================*/


drop table if exists AGENCE;

drop table if exists AGENT;

drop table if exists CLIENTSALARIE;

drop table if exists CLIENTVIP;

drop table if exists COMPTE;

drop table if exists COMPTEEPARGNE;

drop table if exists COMPTEVIP;

/*==============================================================*/
/* Table : AGENCE                                               */
/*==============================================================*/
create table AGENCE
(
   ID_AGENCE             int not null AUTO_INCREMENT,
   NOM_AGENCE           char(200),
   CREDIT_AGENCE_GLOBAL float,
   DEBIT_AGENCE_GLOBAL  float,
   primary key (ID_AGENCE)
);

/*==============================================================*/
/* Table : AGENT                                                */
/*==============================================================*/
create table AGENT
(
   ID_AGENT              int not null AUTO_INCREMENT,
   ID_AGENCE            int not null,
   NOM                  char(200) not null,
   PRENOM               char(200) not null,
   LOGIN                char(200) not null,
   PASSWORD             char(200) not null,
   primary key (ID_AGENT)
);

/*==============================================================*/
/* Table : CLIENTSALARIE                                        */
/*==============================================================*/
create table CLIENTSALARIE
(
   ID_CLIENT            int not null,
   ID_AGENCE            int not null,
   NOM                  char(200),
   PRENOM               char(200),
   CIN                  char(200),
   PROFESSION           char(200),
   SALAIRE              float,
   primary key (ID_CLIENT)
);

/*==============================================================*/
/* Table : CLIENTVIP                                            */
/*==============================================================*/
create table CLIENTVIP
(
   ID_CLIENT            int not null,
   ID_AGENCE            int not null,
   NOM                  char(200),
   PRENOM               char(200),
   CIN                  char(200),
   PROFESSION           char(200),
   NOM_ENTREPRISE       char(200),
   CHIFFRE_AFFAIRE      float,
   NBRE_EMPLOYES        int,
   primary key (ID_CLIENT)
);

/*==============================================================*/
/* Table : COMPTE                                               */
/*==============================================================*/
create table COMPTE
(
   ID_COMPTE            int not null AUTO_INCREMENT,
   ID_CLIENT            int not null,
   ID_AGENCE            int not null,
   RIB                  char(200),
   SOLDE                float,
   DATE_CREATION        date,
   primary key (ID_COMPTE)
);

/*==============================================================*/
/* Table : COMPTEEPARGNE                                        */
/*==============================================================*/
create table COMPTEEPARGNE
(
   ID_COMPTE             int not null AUTO_INCREMENT,
   ID_CLIENT            int not null,
   ID_AGENCE            int not null,
   RIB                  char(200),
   SOLDE                float,
   DATE_CREATION        date,
   MONTANT              float,
   DATE_VERSEMENT       date,
   primary key (ID_COMPTE)
);

/*==============================================================*/
/* Table : COMPTEVIP                                            */
/*==============================================================*/
create table COMPTEVIP
(
   ID_COMPTE            int not null AUTO_INCREMENT,
   ID_CLIENT            int not null,
   ID_AGENCE            int not null,
   RIB                  char(200),
   SOLDE                float,
   DATE_CREATION        date,
   primary key (ID_COMPTE)
);

alter table AGENT add constraint FK_TRAVAILLER foreign key (ID_AGENCE)
      references AGENCE (ID_AGENCE) on delete restrict on update restrict;

alter table CLIENTSALARIE add constraint FK_ETRE2 foreign key (ID_AGENCE)
      references AGENCE (ID_AGENCE) on delete restrict on update restrict;

alter table CLIENTVIP add constraint FK_ETRE foreign key (ID_AGENCE)
      references AGENCE (ID_AGENCE) on delete restrict on update restrict;

alter table COMPTE add constraint FK_APPARTENIR2 foreign key (ID_AGENCE)
      references AGENCE (ID_AGENCE) on delete restrict on update restrict;

alter table COMPTE add constraint FK_POSSEDER2 foreign key (ID_CLIENT)
      references CLIENTSALARIE (ID_CLIENT) on delete restrict on update restrict;

alter table COMPTE add constraint FK_POSSEDER3 foreign key (ID_CLIENT)
      references CLIENTVIP (ID_CLIENT) on delete restrict on update restrict;

alter table COMPTEEPARGNE add constraint FK_APPARTENIR3 foreign key (ID_AGENCE)
      references AGENCE (ID_AGENCE) on delete restrict on update restrict;

alter table COMPTEEPARGNE add constraint FK_POSSEDER4 foreign key (ID_CLIENT)
      references CLIENTSALARIE (ID_CLIENT) on delete restrict on update restrict;

alter table COMPTEEPARGNE add constraint FK_POSSEDER5 foreign key (ID_CLIENT)
      references CLIENTVIP (ID_CLIENT) on delete restrict on update restrict;

alter table COMPTEVIP add constraint FK_APPARTENIR foreign key (ID_AGENCE)
      references AGENCE (ID_AGENCE) on delete restrict on update restrict;

alter table COMPTEVIP add constraint FK_POSSEDER foreign key (ID_CLIENT)
      references CLIENTVIP (ID_CLIENT) on delete restrict on update restrict;

alter table COMPTEVIP add constraint FK_POSSEDER6 foreign key (ID_CLIENT)
      references CLIENTSALARIE (ID_CLIENT) on delete restrict on update restrict;

