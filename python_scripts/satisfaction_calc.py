import csv
from collections import defaultdict

def calculate_user_satisfaction(csv_file):
    headers = [
        "user_id", "concrete_event_id", "points", "is_assigned", "is_impossible",
        "impossibility_justification", "ce_id", "event_id", "place_id", "user_limit",
        "activity_form", "e_id", "group_id", "e_name", "e_description", "color",
        "short_name", "point_limits_class", "point_limits_lecture", "g_id", "g_name",
        "g_description", "max_points_per_concrete", "status"
    ]

    user_data = defaultdict(lambda: {"assigned_points": 0, "total_points": 0})

    sat_acc_ration = 0
    total_num_users = 0
    with open(csv_file, mode="r", encoding="utf-8") as file:
        reader = csv.DictReader(file, fieldnames=headers)
        for row in reader:
            user_id = row["user_id"]
            points = int(row["points"])
            is_assigned = row["is_assigned"].lower() == "true"

            user_data[user_id]["total_points"] += points
            if is_assigned:
                user_data[user_id]["assigned_points"] += points
                print(f"{user_id}: {round(points / 8, 2) * 100}%")
                sat_acc_ration += points / 8
                total_num_users += 1

    satisfation = sat_acc_ration / total_num_users
    print(satisfation)
    print(f"Semester satisfaction level: {round(satisfation, 2) * 100}%")

csv_file_path = "select_from_preference_p_join_concrete_e.csv"
satisfaction_levels = calculate_user_satisfaction(csv_file_path)

