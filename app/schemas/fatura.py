from enum import Enum


class FaturaStatus(str, Enum):
    ABERTA = "ABERTA"
    FECHADA = "FECHADA"
    PAGA = "PAGA"
