from copy import deepcopy
import json
from pathlib import Path
from threading import RLock

DATABASE_DIR = Path(__file__).resolve().parents[1] / "database"
DB_PATH = DATABASE_DIR / "db.json"
SEED_PATH = DATABASE_DIR / "initial_db.json"
COLLECTIONS = ("clientes", "cartoes", "faturas", "pagamentos")

_lock = RLock()


def _read_json(path: Path) -> dict:
    with path.open("r", encoding="utf-8") as file:
        return json.load(file)


def _write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    temp_path = path.with_suffix(".tmp")
    with temp_path.open("w", encoding="utf-8") as file:
        json.dump(data, file, ensure_ascii=False, indent=2)
        file.write("\n")
    temp_path.replace(path)


def _validate_database_shape(data: dict) -> None:
    for collection in COLLECTIONS:
        data.setdefault(collection, [])


def read_database() -> dict:
    with _lock:
        if not DB_PATH.exists():
            reset_database()

        data = _read_json(DB_PATH)
        _validate_database_shape(data)
        return deepcopy(data)


def write_database(data: dict) -> None:
    with _lock:
        _validate_database_shape(data)
        _write_json(DB_PATH, data)


def reset_database() -> dict:
    with _lock:
        seed_data = _read_json(SEED_PATH)
        _validate_database_shape(seed_data)
        _write_json(DB_PATH, seed_data)
        return deepcopy(seed_data)


def next_id(items: list[dict]) -> int:
    if not items:
        return 1
    return max(item["id"] for item in items) + 1
