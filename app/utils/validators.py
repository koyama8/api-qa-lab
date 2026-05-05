import re


def normalize_cpf(cpf: str) -> str:
    digits = re.sub(r"\D", "", cpf)

    if len(digits) != 11 or digits == digits[0] * 11:
        raise ValueError("CPF inválido. Informe 11 dígitos numéricos.")

    return digits
