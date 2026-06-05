import axios from 'axios';

export interface IDataQualityIssue {
  entityType: string;
  severity: string;
  field: string;
  message: string;
  entityId: number;
  entityLabel: string;
}

const baseApiUrl = 'api/data-quality';

export default class DataQualityService {
  runChecks(): Promise<IDataQualityIssue[]> {
    return new Promise<IDataQualityIssue[]>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/checks`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
