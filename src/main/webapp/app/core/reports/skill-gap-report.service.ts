import axios from 'axios';

const baseApiUrl = 'api/reports/position-skill-gaps';
const suggestionApiUrl = 'api/reports/training-suggestions';
const trainingGoalApiUrl = 'api/reports/training-goals/from-suggestion';

export default class SkillGapReportService {
  getReport(positionIds: number[], params?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl, { params: { positionIds, ...params } })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  getSuggestions(positionGaps: any[]): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(suggestionApiUrl, positionGaps)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  createTrainingGoalFromSuggestion(suggestion: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(trainingGoalApiUrl, suggestion)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
