The project testS3 includes two executables: S3FileLoader, SampleProcessor.

usage: S3FileLoader <bucket> <folder> <s3file> <localFilePath>

usage: SampleProcessor <bucket> <folder> <inputs3file> <outputs3file>

The executables need to setup the AWS credential and region configuration.

The following link contains the setup details of the AWS credential and region configuration.
https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html

The input file header is
SAMPLE_ID|PATIENT_NUMBER|ICD_CODE|BODY_SITE|ORDERED_DATE|REPORT_DATE|STATE|RESULT

The output file header is
SAMPLE_ID|PATIENT_NUMBER|ICD_CODE|BODY_SITE|ORDERED_YEAR|REPORT_YEAR|TURNAROUND|STATE|EGFR|PD-L1

The output removed the month and the day from the input fields ORDERED_DATE and REPORT_DATE and only keep the year and saved them in the output fields ORDERED_YEAR and REPORT_YEAR. The turnaround is calculated at the same time and saved in the output field TURNAROUND. This could remove the sensitive information from the input data and keep the turnaround information.

The input field RESULTS is splitted into two fields in the output as EGFR and PD-L1. The data in the two fields are -1, 0 and 1.
1 - positive
-1 - negative
0 - unknown

The output file can be loaded into a MySQL database table (Diagnosis)and queried by sql statements.

The sql statement for the states with the fastest turnaround:
SELECT STATE, TURNAROUND FROM Diagnosis WHERE TURNAROUND=(SELECT MIN(TURNAROUND) FROM Diagnosis);

Assuming that the ICD_CODE of all cancel patients includes C34. The count of cancel patients tested positive with EGFR:
SELECT count(*) FROM Diagnosis WHERE EGFR = 1 AND ICD_CODE LIKE '%C34%';
