import avro.schema
from avro.datafile import DataFileReader, DataFileWriter
from avro.io import DatumReader, DatumWriter
import os


# Get the directory where this script is located
script_dir = os.path.dirname(os.path.abspath(__file__))
# Go up one level to app directory, then into resources
app_dir = os.path.dirname(script_dir)
resources_dir = os.path.join(app_dir, 'resources')

avro_path = os.path.join(resources_dir, 'email.avro')
schema_path = os.path.join(resources_dir, 'email.avsc')

# Load the email schema
def try_avro():
    try:
        with open(schema_path, 'r') as f:
            schema = avro.schema.parse(f.read())
            
            writer = DataFileWriter(open(avro_path, 'wb'), DatumWriter(), schema)
            writer.append(
                {
                    "email": "mohamed.sweelam@gmail.com",
                    "title": "welcome to avro",
                    "body": "This email introduces you to avro",
                    "EventType": "EMAIL_WELCOMED"
                }
            )
            writer.close()

    except FileNotFoundError:
        print(f"❌ Avro file not found: {avro_path}")
    except Exception as e:
        print(f"❌ Error writing Avro file: {e}")

def read_avro_file():
    """Read and display the contents of the Avro file"""
    try:
        with open(avro_path, 'rb') as avro_file:
            reader = DataFileReader(avro_file, DatumReader())
            
            print(f"\n�� Reading from {avro_path}:")
            for record in reader:
                print(f"Email: {record['email']}")
                print(f"Title: {record['title']}")
                print(f"Body: {record['body']}")
                print(f"Event Type: {record['EventType']}")
                print("-" * 40)
            
            reader.close()
            
    except FileNotFoundError:
        print(f"❌ Avro file not found: {avro_path}")
    except Exception as e:
        print(f"❌ Error reading Avro file: {e}")


if __name__ == "__main__":
    try_avro()
    read_avro_file()